package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons
import software.amazon.smithy.intellij.SmithyPreludeShapes

/**
 * A [shape](https://awslabs.github.io/smithy/1.0/spec/core/model.html#shapes) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstShape(
    override val ast: SmithyAst, override val file: PsiFile, override val shapeId: String, val shape: SmithyAst.Shape
) : FakePsiElement(), SmithyAstDefinition, SmithyShapeDefinition {
    override val type = shape.type
    override val members = shape.let { it as? SmithyAst.AggregateShape }?.let {
        (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
            SmithyAstMember(this, memberName, reference)
        }
    } ?: emptyList()
    override val documentation = (shape.traits?.get(SmithyPreludeShapes.DOCUMENTATION) as? String)?.let {
        SmithyAstDocumentation(this, it)
    }
    override val declaredTraits = (shape.traits ?: emptyMap()).entries.map { (key, value) ->
        SmithyAstTrait(this, key, value)
    }
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