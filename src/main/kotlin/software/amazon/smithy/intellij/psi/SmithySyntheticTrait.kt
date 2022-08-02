package software.amazon.smithy.intellij.psi

/**
 * A synthetic [SmithyTraitDefinition] (mostly used for the implicit traits introduced by IDL syntax sugar or the result of merged traits).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithySyntheticTrait(
    val target: SmithyDefinition,
    override val declaredNamespace: String?,
    override val resolvedNamespace: String?,
    override val shapeName: String,
    override val value: SmithyValueDefinition = SmithySyntheticValue.Object()
) : SmithySyntheticElement(), SmithyTraitDefinition {
    constructor(
        target: SmithyDefinition,
        namespace: String,
        shapeName: String,
        value: SmithyValueDefinition = SmithySyntheticValue.Object()
    ) : this(target, namespace, namespace, shapeName, value)

    init {
        if (value is SmithySyntheticValue) value.scope(this)
    }

    override fun getParent() = target
    override fun getPresentableText() = shapeName
    override fun getLocationString() = target.name
    override fun getIcon(unused: Boolean) = getIcon(0)
}
