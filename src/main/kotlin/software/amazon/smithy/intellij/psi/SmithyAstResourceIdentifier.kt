package software.amazon.smithy.intellij.psi

/**
 * A [resource identifier](https://awslabs.github.io/smithy/1.0/spec/core/model.html#resource-identifiers) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstResourceIdentifier(
    override val enclosingShape: SmithyAstShape,
    val id: String,
    override val declaredTarget: SmithyAstTarget
) : SmithySyntheticElement(), SmithyResourceIdentifierDefinition {
    private val nameIdentifier = object : SmithySyntheticElement() {
        override fun getText() = name
        override fun getParent() = this@SmithyAstResourceIdentifier
    }

    override fun getParent() = enclosingShape
    override fun getName() = id
    override fun getNameIdentifier() = nameIdentifier
}
