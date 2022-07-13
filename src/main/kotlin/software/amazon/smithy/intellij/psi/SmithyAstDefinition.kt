package software.amazon.smithy.intellij.psi

/**
 * A definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyAstMember
 * @see SmithyAstShape
 */
sealed interface SmithyAstDefinition : SmithyDefinition {
    override fun getName(): String
}