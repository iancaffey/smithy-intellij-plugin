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

    fun getMembers(shape: SmithyShapeDefinition): List<SmithyMemberDefinition> = getCachedValue(shape) {
        CachedValueProvider.Result.create(getMembers(shape, mutableSetOf()), dependencies)
    }

    fun getTraits(member: SmithyMemberDefinition): List<SmithyTraitDefinition> = getCachedValue(member) {
        CachedValueProvider.Result.create(collectTraits(member), dependencies)
    }

    private fun collectTraits(member: SmithyMemberDefinition): List<SmithyTraitDefinition> {
        //Synthetic members are not re-declared, so they only inherit the original member traits
        //Declared members inherit from the last mixin member (since re-declared members supersede the previous member)
        val explicitTraits = listOf(member.appliedTraits, member.declaredTraits, member.syntheticTraits).flatten()
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

    fun getTraits(shape: SmithyShapeDefinition): List<SmithyTraitDefinition> = getCachedValue(shape) {
        CachedValueProvider.Result.create(getTraits(shape, mutableSetOf()), dependencies)
    }

    private fun getMembers(shape: SmithyShapeDefinition, visited: MutableSet<String>): List<SmithyMemberDefinition> {
        val declaredMembers = shape.declaredMembers
        val members = mutableMapOf<String, SmithyMemberDefinition>()
        shape.mixins.forEach { target ->
            //Note: all mixins _should_ have this trait, but invalid mixins will be ignored here
            val mixin = target.resolve()?.takeIf { it.hasTrait("smithy.api", "mixin") } ?: return@forEach
            getMembers(mixin, visited).forEach { inheritedMember ->
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

    private fun getTraits(shape: SmithyShapeDefinition, visited: MutableSet<String>): List<SmithyTraitDefinition> {
        if (!visited.contains(shape.shapeId)) visited += shape.shapeId else return emptyList()
        val explicitTraits = listOf(shape.appliedTraits, shape.declaredTraits, shape.syntheticTraits).flatten()
        val inheritedTraits = mutableMapOf<String, SmithyTraitDefinition>()
        shape.mixins.forEach { target ->
            val mixin = target.resolve() ?: return@forEach
            //Note: all mixins _should_ have this trait, but invalid mixins will be ignored here
            val mixinTrait = mixin.findTrait("smithy.api", "mixin") ?: return@forEach
            val ignoredTraits = mutableSetOf("smithy.api#mixin")
            mixinTrait.value.fields["localTraits"]?.values?.forEach {
                it.asString()?.let { shapeId -> ignoredTraits += shapeId }
            }
            getTraits(mixin, visited).forEach {
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
            return@mapNotNull if (value != null) SmithySyntheticTrait(
                parent,
                first.declaredNamespace,
                first.resolvedNamespace,
                first.shapeName,
                value
            ) else null
        }
    }
}