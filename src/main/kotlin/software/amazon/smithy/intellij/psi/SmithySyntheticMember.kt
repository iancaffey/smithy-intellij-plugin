package software.amazon.smithy.intellij.psi

/**
 * A synthetic [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition which refines a `document` member.
 *
 * Certain members cannot be modeled directly in Smithy as there is no concept of generics (e.g. example input/output/error).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithySyntheticMember(
    override val enclosingShape: SmithyShapeDefinition,
    val memberName: String,
) : SmithySyntheticElement(), SmithyMemberDefinition {
    private val id = object : SmithySyntheticElement() {
        override fun getText() = name
        override fun getParent() = this@SmithySyntheticMember
    }
    override lateinit var declaredTarget: SmithyShapeTarget
    override val resolvedTarget get() = declaredTarget
    override val declaredTraits = emptyList<SmithyTraitDefinition>()
    override val syntheticTraits = emptyList<SmithyTraitDefinition>()
    override fun getName() = memberName
    override fun getNameIdentifier() = id
    override fun getParent() = enclosingShape
    override fun getPresentableText(): String = "$name: ${resolvedTarget.shapeName}"
    override fun getLocationString() = enclosingShape.shapeName
    override fun getIcon(unused: Boolean) = getIcon(0)
}