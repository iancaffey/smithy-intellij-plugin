package software.amazon.smithy.intellij.psi

import software.amazon.smithy.intellij.SmithyShapeResolver.getDefinitions

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) target in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAstTarget(val member: SmithyAstMember, val shapeId: String) : SmithySyntheticElement(), SmithyShapeTarget {
    private val parts = shapeId.split('#', limit = 2)
    override val shapeName = parts[1]
    override val declaredNamespace = parts[0]
    override val resolvedNamespace = parts[0]
    override fun getName() = shapeName
    override fun getParent() = member
    override fun resolve() = getDefinitions(this, declaredNamespace, shapeName).firstOrNull()
}