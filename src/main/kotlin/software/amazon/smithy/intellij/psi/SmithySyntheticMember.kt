package software.amazon.smithy.intellij.psi

/**
 * A synthetic [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition introduced by a mixin or resource reference.
 *
 * [SmithySyntheticMember] functions exactly as the original [SmithyMemberDefinition], but has an [SmithyMemberDefinition.enclosingShape] which reflects the shape it was "copied".
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithySyntheticMember(
    override val enclosingShape: SmithyShapeDefinition, val original: SmithyMemberDefinition
) : SmithySyntheticElement(), SmithyMemberDefinition {
    private val id = object : SmithySyntheticElement() {
        override fun getText() = name
        override fun getParent() = this@SmithySyntheticMember
    }
    override val declaredTarget get() = original.declaredTarget
    override val resolvedTarget get() = original.declaredTarget
    override val declaredTraits = emptyList<SmithyTraitDefinition>()
    override val syntheticTraits = emptyList<SmithyTraitDefinition>()
    override val documentation get() = original.documentation
    override fun getName() = original.name
    override fun getNameIdentifier() = id
    override fun getParent() = enclosingShape
    override fun getPresentableText(): String = resolvedTarget?.let { "$name: ${it.shapeName}" } ?: name
    override fun getLocationString() = enclosingShape.shapeName
    override fun getIcon(unused: Boolean) = getIcon(0)
}