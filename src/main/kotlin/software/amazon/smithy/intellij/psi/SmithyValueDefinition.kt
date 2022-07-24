package software.amazon.smithy.intellij.psi

import java.math.BigDecimal

/**
 * A [node value](https://awslabs.github.io/smithy/1.0/spec/core/model.html#node-values) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyValue
 * @see SmithyAstValue
 */
interface SmithyValueDefinition : SmithyElement {
    val type: SmithyValueType
    val values: List<@JvmWildcard SmithyValueDefinition> get() = emptyList()
    val fields: Map<String, @JvmWildcard SmithyValueDefinition> get() = emptyMap()
    fun asBoolean(): Boolean? = null
    fun asNumber(): BigDecimal? = null
    fun asString(): String? = null
}

/**
 * An enumeration of [SmithyValueDefinition] types.
 *
 * All value providers within [SmithyValueDefinition] are safe across all subclasses, but inspecting the type can
 * provide for differentiating between defaults vs. empty/null values.
 *
 * @author Ian Caffey
 * @since 1.0
 */
enum class SmithyValueType {
    ARRAY, BOOLEAN, NULL, NUMBER, OBJECT, STRING
}
