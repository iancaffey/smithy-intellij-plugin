package software.amazon.smithy.intellij.psi

/**
 * A [resource property](https://awslabs.github.io/smithy/1.0/spec/core/model.html#resource) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyResourceProperty
 * @see SmithyAstResourceProperty
 */
interface SmithyResourcePropertyDefinition : SmithyElidedMemberTarget {
    override val declaredTarget: SmithyShapeTarget
    override val resolvedTarget: SmithyShapeTarget get() = declaredTarget
    override val appliedTraits get() = emptyList<SmithyTraitDefinition>()
    override val declaredTraits get() = emptyList<SmithyTraitDefinition>()
    override val syntheticTraits get() = emptyList<SmithyTraitDefinition>()
    override val traits get() = emptyList<SmithyTraitDefinition>()
}