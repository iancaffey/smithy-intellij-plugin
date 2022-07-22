package software.amazon.smithy.intellij.psi

/**
 * A [shape](https://awslabs.github.io/smithy/1.0/spec/core/model.html#shapes) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyShape
 * @see SmithyAstShape
 */
interface SmithyShapeDefinition : SmithyDefinition {
    val type: String
    val shapeId: String
    val shapeName: String
    val namespace: String
    val members: List<@JvmWildcard SmithyMemberDefinition>
    val enumValues
        get() = findTrait("smithy.api", "enum")?.value?.values?.mapNotNull {
            it.fields["value"]?.asString()
        } ?: emptyList()

    fun getMember(name: String): SmithyMemberDefinition? = members.find { it.name == name }
}