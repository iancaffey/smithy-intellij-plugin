package software.amazon.smithy.intellij.psi

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyMember
 * @see SmithyAstMember
 */
interface SmithyMemberDefinition : SmithyDefinition {
    val enclosingShape: SmithyShapeDefinition
    val targetShapeName: String
    val declaredTargetNamespace: String?
    val resolvedTargetNamespace: String?
    fun resolve(): SmithyShapeDefinition?
}