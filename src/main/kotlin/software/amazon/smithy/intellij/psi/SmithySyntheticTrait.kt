package software.amazon.smithy.intellij.psi

/**
 * A synthetic [SmithyTraitDefinition] (mostly used for the implicit traits introduced by IDL syntax sugar or the result of merged traits).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithySyntheticTrait(
    val target: SmithyDefinition,
    override val declaredNamespace: String,
    override val shapeName: String,
    override val value: SmithyValueDefinition = SmithySyntheticValue.Object()
) : SmithySyntheticElement(), SmithyTraitDefinition {
    init {
        if (value is SmithySyntheticValue) value.scope(this)
    }

    override val resolvedNamespace = declaredNamespace
    override fun getParent() = target
}
