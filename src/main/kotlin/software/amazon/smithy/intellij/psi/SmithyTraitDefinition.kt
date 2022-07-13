package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyShapeResolver

/**
 * A [trait](https://awslabs.github.io/smithy/1.0/spec/core/model.html#traits) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
interface SmithyTraitDefinition : SmithyElement {
    val shapeName: String
    val declaredNamespace: String?
    val resolvedNamespace: String?
    fun resolve(): SmithyShapeDefinition? {
        val namespace = resolvedNamespace ?: return null
        return SmithyShapeResolver.getDefinitions(this, namespace, shapeName).takeIf { it.size == 1 }?.first()
    }

    //TODO: once a unified model is created for the node value(s) between AST + IDL, move the doc string logic into the value
    fun toDocString(): String
}