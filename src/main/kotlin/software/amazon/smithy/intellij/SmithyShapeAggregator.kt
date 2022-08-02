package software.amazon.smithy.intellij

import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager.getCachedValue
import com.intellij.psi.util.PsiModificationTracker
import software.amazon.smithy.intellij.psi.SmithyDefinition
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithySyntheticMember
import software.amazon.smithy.intellij.psi.SmithySyntheticTrait
import software.amazon.smithy.intellij.psi.SmithySyntheticValue
import software.amazon.smithy.intellij.psi.SmithyTraitDefinition
import software.amazon.smithy.intellij.psi.SmithyValueDefinition

/**
 * A utility class providing API to merge all definitions along with any inherited members and traits for a [SmithyShapeDefinition].
 *
 * @author Ian Caffey
 * @since 1.0
 * @see <a href="https://awslabs.github.io/smithy/1.0/spec/core/model.html#merging-model-files">Merging model files</a>
 */
object SmithyShapeAggregator {
    private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)

    fun hasCycle(shape: SmithyShapeDefinition): Boolean = getCachedValue(shape) {
        CachedValueProvider.Result.create(hasCycle(shape, mutableSetOf()), dependencies)
    }

    fun getMembers(shape: SmithyShapeDefinition): List<SmithyMemberDefinition> = getCachedValue(shape) {
        CachedValueProvider.Result.create(findMembers(shape), dependencies)
    }

    fun getTraits(member: SmithyMemberDefinition): List<SmithyTraitDefinition> = getCachedValue(member) {
        CachedValueProvider.Result.create(collectTraits(member), dependencies)
    }

    fun getTraits(shape: SmithyShapeDefinition): List<SmithyTraitDefinition> = getCachedValue(shape) {
        CachedValueProvider.Result.create(findTraits(shape), dependencies)
    }

    private fun findMembers(shape: SmithyShapeDefinition): List<SmithyMemberDefinition> {
        val declaredMembers = shape.declaredMembers
        if (hasCycle(shape)) return declaredMembers
        val members = mutableMapOf<String, SmithyMemberDefinition>()
        shape.mixins.forEach { target ->
            //Note: all mixins _should_ have this trait, but invalid mixins will be ignored here
            val mixin = target.resolve()?.takeIf { it.hasTrait("smithy.api", "mixin") } ?: return@forEach
            findMembers(mixin).forEach { inheritedMember ->
                //Note: declared members are kept as-is (even if they have mixins), as the original definition supports
                //locating inherited traits (so we only need to introduce new members from mixins here)
                members[inheritedMember.name] = declaredMembers.find {
                    it.name == inheritedMember.name
                } ?: SmithySyntheticMember(shape, inheritedMember)
            }
        }
        declaredMembers.forEach { if (it.name !in members) members[it.name] = it }
        return members.values.toList()
    }

    private fun findTraits(shape: SmithyShapeDefinition): List<SmithyTraitDefinition> {
        val explicitTraits = listOf(shape.syntheticTraits, shape.declaredTraits, shape.appliedTraits).flatten()
        if (hasCycle(shape)) return merge(shape, explicitTraits)
        val inheritedTraits = mutableMapOf<String, SmithyTraitDefinition>()
        shape.mixins.forEach { target ->
            val mixin = target.resolve() ?: return@forEach
            //Note: all mixins _should_ have this trait, but invalid mixins will be ignored here
            val mixinTrait = mixin.findTrait("smithy.api", "mixin") ?: return@forEach
            val ignoredTraits = mutableSetOf("smithy.api#mixin")
            mixinTrait.value.fields["localTraits"]?.values?.forEach {
                it.asString()?.let { shapeId -> ignoredTraits += shapeId }
            }
            findTraits(mixin).forEach {
                val namespace = it.resolvedNamespace
                if (namespace != null
                    && "$namespace#${it.shapeName}" !in ignoredTraits
                    && explicitTraits.none { explicit -> explicit.shapeName == it.shapeName && explicit.resolvedNamespace == namespace }
                ) {
                    inheritedTraits["${namespace}#${it.shapeName}"] = it
                }
            }
        }
        return merge(shape, listOf(explicitTraits, inheritedTraits.values).flatten())
    }

    private fun hasCycle(shape: SmithyShapeDefinition, visited: MutableSet<String>): Boolean {
        if (visited.contains(shape.shapeId)) return true else visited += shape.shapeId
        return shape.mixins.any {
            val target = it.resolve()
            target != null && hasCycle(target, visited)
        }
    }

    private fun collectTraits(member: SmithyMemberDefinition): List<SmithyTraitDefinition> {
        //Synthetic members are not re-declared, so they only inherit the original member traits
        //Declared members inherit from the last mixin member (since re-declared members supersede the previous member)
        val explicitTraits = listOf(member.syntheticTraits, member.declaredTraits, member.appliedTraits).flatten()
        val inheritedTraits = when (member) {
            is SmithySyntheticMember -> collectTraits(member.original)
            else -> member.enclosingShape.mixins.mapNotNull {
                it.resolve()?.getMember(member.name)
            }.lastOrNull()?.traits ?: emptyList()
        }.filter {
            explicitTraits.none { explicit ->
                explicit.shapeName == it.shapeName && explicit.resolvedNamespace == it.resolvedNamespace
            }
        }
        val resolvedTraits = mutableListOf<SmithyTraitDefinition>()
        resolvedTraits += explicitTraits
        resolvedTraits += inheritedTraits
        //Note: enum members receive an implicit/synthetic enumValue trait (where the value is the member name), but only
        //if no enumValue trait was applied
        if (member.enclosingShape.type == "enum"
            && resolvedTraits.none { it.shapeName == "enumValue" && it.resolvedNamespace == "smithy.api" }
        ) {
            resolvedTraits += SmithySyntheticTrait(
                member,
                "smithy.api",
                "enumValue",
                SmithySyntheticValue.String(member.name)
            )
        }
        return merge(member, resolvedTraits)
    }

    private fun merge(parent: SmithyDefinition, traits: List<SmithyTraitDefinition>): List<SmithyTraitDefinition> {
        val traitsByShapeId = traits.groupBy { "${it.resolvedNamespace}#${it.shapeName}" }
        return traitsByShapeId.values.mapNotNull { definitions ->
            //Note: Traits are de-duped by "source position" (filename, line, column)
            val distinct = definitions.distinctBy { it.containingFile.name to it.textOffset }
            val first = distinct.first()
            if (distinct.size == 1) return@mapNotNull first
            var value: SmithyValueDefinition? = first.value
            for (definition in distinct) {
                if (value == null) break
                value = value.merge(definition.value)
            }
            //Note: if the trait value has a merge conflict, the entire trait gets dropped here
            value?.let {
                SmithySyntheticTrait(parent, first.declaredNamespace, first.resolvedNamespace, first.shapeName, it)
            }
        }
    }
}