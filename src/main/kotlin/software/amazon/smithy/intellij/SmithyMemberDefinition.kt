package software.amazon.smithy.intellij

import software.amazon.smithy.intellij.psi.SmithyMember

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyMember
 * @see SmithyExternalMember
 */
interface SmithyMemberDefinition : SmithyDefinition {
    val enclosingShape: SmithyShapeDefinition
    val targetShapeId: String
}