package software.amazon.smithy.intellij

import software.amazon.smithy.intellij.psi.SmithyShape

/**
 * A [shape](https://awslabs.github.io/smithy/1.0/spec/core/model.html#shapes) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyShape
 * @see SmithyExternalShape
 */
interface SmithyShapeDefinition : SmithyDefinition {
    val shapeId: String
    val namespace: String
    val members: List<@JvmWildcard SmithyMemberDefinition>
    fun hasTrait(id: String): Boolean
    fun getMember(name: String): SmithyMemberDefinition? = members.find { it.name == name }
}