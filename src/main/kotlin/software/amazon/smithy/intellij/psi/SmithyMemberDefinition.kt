package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyAppliedTraitResolver.getAppliedTraits
import software.amazon.smithy.intellij.SmithyShapeAggregator.getTraits

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyAstMember
 * @see SmithyContainerMember
 * @see SmithyElidedMember
 * @see SmithyEnumMember
 * @see SmithyIntEnumMember
 * @see SmithySyntheticMember
 */
interface SmithyMemberDefinition : SmithyElidedMemberTarget {
    override val traits: List<SmithyTraitDefinition> get() = getTraits(this)
    override val appliedTraits get() = getAppliedTraits(this)
    override val resolvedTarget: SmithyShapeTarget? get() = declaredTarget
    val defaultValue: SmithyValueDefinition? get() = findTrait("smithy.api", "default")?.value
    val enumValue: SmithyValueDefinition? get() = findTrait("smithy.api", "enumValue")?.value
}