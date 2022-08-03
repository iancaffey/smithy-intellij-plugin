package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyAppliedTraitResolver.getAppliedTraits
import software.amazon.smithy.intellij.SmithyShapeAggregator.getMembers
import software.amazon.smithy.intellij.SmithyShapeAggregator.getTraits
import software.amazon.smithy.intellij.generateLink

/**
 * A [shape](https://awslabs.github.io/smithy/1.0/spec/core/model.html#shapes) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyShape
 * @see SmithyAstShape
 * @see SmithySyntheticShape
 */
interface SmithyShapeDefinition : SmithyDefinition {
    val href: String get() = generateLink("${namespace}#${shapeName}", shapeName)
    override val appliedTraits get() = getAppliedTraits(this)
    override val traits: List<SmithyTraitDefinition> get() = getTraits(this)
    val type: String
    val shapeId: String
    val shapeName: String
    val namespace: String
    val resource: SmithyShapeTarget? get() = null
    val identifiers: List<@JvmWildcard SmithyResourceIdentifierDefinition> get() = emptyList()
    val members: List<SmithyMemberDefinition> get() = getMembers(this)
    val declaredMembers: List<@JvmWildcard SmithyMemberDefinition>
    val mixins: List<@JvmWildcard SmithyShapeTarget>
    fun getIdentifier(name: String): SmithyResourceIdentifierDefinition? = identifiers.find { it.name == name }
    fun getMember(name: String): SmithyMemberDefinition? = members.find { it.name == name }
}