package software.amazon.smithy.intellij

import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager.getCachedValue
import com.intellij.psi.util.PsiModificationTracker
import software.amazon.smithy.intellij.index.SmithyAppliedTraitMemberIndex
import software.amazon.smithy.intellij.index.SmithyAppliedTraitShapeIndex
import software.amazon.smithy.intellij.index.SmithyAstAppliedTraitIndex
import software.amazon.smithy.intellij.psi.SmithyAstTrait
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyTraitDefinition

/**
 * A utility class providing methods to resolve externally applied traits to shapes and members.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyAppliedTraitResolver {
    private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
    fun getAppliedTraits(member: SmithyMemberDefinition): List<SmithyTraitDefinition> = getCachedValue(member) {
        val psi = PsiManager.getInstance(member.project)
        val namespace = member.enclosingShape.namespace
        val shapeName = member.enclosingShape.shapeName
        val memberName = member.name
        val scope = member.resolveScope
        val appliedTraits = mutableListOf<SmithyTraitDefinition>()
        SmithyAppliedTraitMemberIndex.getFiles(shapeName, memberName, scope).forEach { file ->
            (psi.findFile(file) as? SmithyFile)?.model?.appliedTraits?.forEach {
                val memberId = it.memberId
                val shapeId = memberId?.shapeId
                if (memberId != null && memberId.memberName == memberName
                    && shapeId != null && shapeId.shapeName == shapeName && shapeId.resolvedNamespace == namespace
                ) {
                    appliedTraits += it.trait
                }
            }
        }
        SmithyAstAppliedTraitIndex.forEach(namespace, shapeName, memberName, scope) {
            it.traits?.forEach { (traitShapeId, value) ->
                appliedTraits += SmithyAstTrait(member, traitShapeId, value)
            }
        }
        CachedValueProvider.Result.create(appliedTraits, dependencies)
    }

    fun getAppliedTraits(shape: SmithyShapeDefinition): List<SmithyTraitDefinition> = getCachedValue(shape) {
        val psi = PsiManager.getInstance(shape.project)
        val namespace = shape.namespace
        val shapeName = shape.shapeName
        val scope = shape.resolveScope
        val appliedTraits = mutableListOf<SmithyTraitDefinition>()
        SmithyAppliedTraitShapeIndex.getFiles(shapeName, scope).forEach { file ->
            (psi.findFile(file) as? SmithyFile)?.model?.appliedTraits?.forEach {
                val shapeId = it.shapeId
                if (shapeId != null && shapeId.shapeName == shapeName && shapeId.resolvedNamespace == namespace) {
                    appliedTraits += it.trait
                }
            }
        }
        SmithyAstAppliedTraitIndex.forEach(namespace, shapeName, scope) {
            it.traits?.forEach { (traitShapeId, value) ->
                appliedTraits += SmithyAstTrait(shape, traitShapeId, value)
            }
        }
        CachedValueProvider.Result.create(appliedTraits, dependencies)
    }
}