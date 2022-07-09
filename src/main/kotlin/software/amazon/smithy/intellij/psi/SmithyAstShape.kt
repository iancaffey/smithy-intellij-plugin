package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons

/**
 * A [FakePsiElement] which represents a shape from [SmithyAst].
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstShape(
    val ast: SmithyAst, val file: PsiFile, override val shapeId: String, val shape: SmithyAst.Shape
) : FakePsiElement(), SmithyShapeDefinition {
    override val type = shape.type
    override val members = shape.let { it as? SmithyAst.AggregateShape }?.let {
        (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
            SmithyAstMember(this, memberName, reference)
        }
    } ?: emptyList()
    override val namespace = shapeId.split('#', limit = 2)[0]
    override fun getName() = shapeId.split('#', limit = 2)[1]
    override fun hasTrait(id: String) = shape.traits?.keys?.any { it == id } == true
    override fun getMember(name: String): SmithyAstMember? {
        val key = if (shape is SmithyAst.Map) "value" else name
        return members.find { it.name == key }
    }

    override fun getParent() = file
    override fun getLocationString() = namespace
    override fun getIcon(unused: Boolean) = SmithyIcons.SHAPE
    override fun toString() = shapeId
}