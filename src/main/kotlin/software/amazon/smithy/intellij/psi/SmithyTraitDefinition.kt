package software.amazon.smithy.intellij.psi

/**
 * A [trait](https://awslabs.github.io/smithy/1.0/spec/core/model.html#traits) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
interface SmithyTraitDefinition : SmithyElement {
    val shapeId: String
    val shapeName: String

    //TODO: once a unified model is created for the node value(s) between AST + IDL, move the doc string logic into the value
    fun toDocString(): String
}