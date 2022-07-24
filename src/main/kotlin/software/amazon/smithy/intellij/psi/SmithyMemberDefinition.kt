package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyAppliedTraitResolver.getAppliedTraits

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyAstMember
 * @see SmithyContainerMember
 * @see SmithyEnumMember
 * @see SmithyIntEnumMember
 */
interface SmithyMemberDefinition : SmithyDefinition {
    override val appliedTraits get() = getAppliedTraits(this)
    val enclosingShape: SmithyShapeDefinition
    val targetShapeName: String
    val declaredTargetNamespace: String?
    val resolvedTargetNamespace: String?
    fun resolve(): SmithyShapeDefinition?
}