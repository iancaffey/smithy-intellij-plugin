package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstMember(
    override val enclosingShape: SmithyAstShape, val memberName: String, val reference: SmithyAst.Reference
) : SmithySyntheticElement(), SmithyMemberDefinition {
    private val id = object : SmithySyntheticElement() {
        override fun getText() = name
        override fun getParent() = this@SmithyAstMember
    }

    override val declaredTarget = SmithyAstTarget(this, reference.target)
    override val resolvedTarget = declaredTarget
    override val declaredTraits = (reference.traits ?: emptyMap()).entries.map { (key, value) ->
        SmithyAstTrait(this, key, value)
    }

    override fun getName() = memberName
    override fun getNameIdentifier() = id
    override fun getParent(): SmithyAstShape = enclosingShape
    override fun getLocationString(): String = enclosingShape.locationString
    override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
    override fun toString() = "$parent$$memberName"
}