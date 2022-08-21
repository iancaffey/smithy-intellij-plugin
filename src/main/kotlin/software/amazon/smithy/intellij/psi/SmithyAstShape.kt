package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiFile
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
) : SmithySyntheticElement(), SmithyShapeDefinition {
    private val id = object : SmithySyntheticElement() {
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
    override val input = (shape as? SmithyAst.Operation)?.input?.let { SmithyAstTarget(this, it.target) }
    override val output = (shape as? SmithyAst.Operation)?.output?.let { SmithyAstTarget(this, it.target) }
    override val identifiers: List<SmithyAstResourceIdentifier> = shape.let { it as? SmithyAst.Resource }?.let {
        it.identifiers?.entries?.map { (name, reference) ->
            SmithyAstResourceIdentifier(this, name, SmithyAstTarget(this, reference.target))
        }
    } ?: emptyList()
    override val properties: List<SmithyAstResourceProperty> = shape.let { it as? SmithyAst.Resource }?.let {
        it.properties?.entries?.map { (name, reference) ->
            SmithyAstResourceProperty(this, name, SmithyAstTarget(this, reference.target))
        }
    } ?: emptyList()
    override val declaredMembers = shape.let { it as? SmithyAst.ComplexShape }?.let {
        (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
            SmithyAstMember(this, memberName, reference)
        }
    } ?: emptyList()
    override val mixins: List<SmithyShapeTarget> = shape.mixins?.map {
        SmithyAstTarget(this, it.target)
    } ?: emptyList()
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