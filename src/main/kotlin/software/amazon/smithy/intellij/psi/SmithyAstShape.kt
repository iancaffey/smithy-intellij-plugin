package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons

/**
 * A [shape](https://awslabs.github.io/smithy/1.0/spec/core/model.html#shapes) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstShape(
    override val shapeId: String, val shape: SmithyAst.Shape
) : FakePsiElement(), SmithyAstDefinition, SmithyShapeDefinition {
    private val id = object : FakePsiElement() {
        override fun getText() = name
        override fun getParent() = this@SmithyAstShape
    }

    //Note: SmithyAstShape loaded from SmithyAstShapeIndex will have a lazily-initialized parent file, so that way
    //dependencies can be cached in a FileBasedIndex and a shallow copy performed to get a valid PSI element on reads
    private var file: PsiFile? = null
    private val parts = shapeId.split('#', limit = 2)
    override val shapeName = parts[1]
    override val namespace = parts[0]
    override val type = shape.type
    override val members = shape.let { it as? SmithyAst.AggregateShape }?.let {
        (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
            SmithyAstMember(this, memberName, reference)
        }
    } ?: emptyList()
    override val documentation = shape.traits?.get("smithy.api#documentation")?.asString()?.let {
        SmithyAstDocumentation(this, it)
    }
    override val declaredTraits = (shape.traits ?: emptyMap()).entries.map { (key, value) ->
        SmithyAstTrait(this, key, value)
    }

    override fun getName() = shapeName
    override fun getNameIdentifier() = id
    override fun getMember(name: String) = members.find { it.name == name }
    override fun getParent() = file
    override fun getLocationString() = namespace
    override fun getIcon(unused: Boolean) = SmithyIcons.SHAPE
    override fun toString() = shapeId
    fun within(file: PsiFile) = SmithyAstShape(shapeId, shape).also { it.file = file }
}