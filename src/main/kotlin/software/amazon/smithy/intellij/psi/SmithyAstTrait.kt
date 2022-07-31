package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyAst

/**
 * A [trait](https://awslabs.github.io/smithy/1.0/spec/core/model.html#traits) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstTrait(
    val target: SmithyDefinition, val shapeId: String, val body: SmithyAst.Value
) : SmithySyntheticElement(), SmithyTraitDefinition {
    override val value = SmithyAstValue(this, body)
    private val parts = shapeId.split('#', limit = 2)
    override val shapeName = parts[1]
    override val declaredNamespace = parts[0]
    override val resolvedNamespace = parts[0]
    override fun getName() = shapeName
    override fun getParent() = target
    override fun getPresentableText() = shapeName
    override fun getLocationString() = target.name
    override fun getIcon(unused: Boolean) = getIcon(0)
}
