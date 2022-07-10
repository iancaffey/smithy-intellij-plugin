package software.amazon.smithy.intellij.psi

import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons
import software.amazon.smithy.intellij.SmithyPreludeShapes
import software.amazon.smithy.intellij.SmithyShapeResolver

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstMember(
    override val enclosingShape: SmithyAstShape, val memberName: String, val reference: SmithyAst.Reference
) : FakePsiElement(), SmithyAstDefinition, SmithyMemberDefinition {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(member: SmithyAstMember) = CachedValueProvider {
            val results = SmithyShapeResolver.resolve(member)
            CachedValueProvider.Result.create(if (results.size == 1) results.first() else null, dependencies)
        }
    }

    override val ast = enclosingShape.ast
    override val file = enclosingShape.file
    override val targetShapeId = reference.target
    override val targetShapeName = targetShapeId.split('#', limit = 2)[1]
    override val documentation = (reference.traits?.get(SmithyPreludeShapes.DOCUMENTATION) as? String)?.let {
        SmithyAstDocumentation(this, it)
    }
    override val declaredTraits = (reference.traits ?: emptyMap()).entries.map { (key, value) ->
        SmithyAstTrait(this, key, value)
    }

    override fun getName() = memberName
    override fun getParent(): SmithyAstShape = enclosingShape
    override fun getLocationString(): String = enclosingShape.locationString
    override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
    override fun findTrait(shapeId: String) = declaredTraits.find { it.shapeId == shapeId }
    override fun resolve(): SmithyShapeDefinition? = CachedValuesManager.getCachedValue(this, resolver(this))
    override fun toString() = "$parent$$memberName"
}