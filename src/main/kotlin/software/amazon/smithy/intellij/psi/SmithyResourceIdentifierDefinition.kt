package software.amazon.smithy.intellij.psi

/**
 * A [resource identifier](https://awslabs.github.io/smithy/1.0/spec/core/model.html#resource-identifiers) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyResourceIdentifier
 * @see SmithyAstResourceIdentifier
 */
interface SmithyResourceIdentifierDefinition : SmithyElidedMemberTarget {
    override val declaredTarget: SmithyShapeTarget
    override val resolvedTarget: SmithyShapeTarget get() = declaredTarget
    override val appliedTraits get() = emptyList<SmithyTraitDefinition>()
    override val declaredTraits get() = emptyList<SmithyTraitDefinition>()
    override val syntheticTraits get() = emptyList<SmithyTraitDefinition>()
    override val traits get() = emptyList<SmithyTraitDefinition>()
}