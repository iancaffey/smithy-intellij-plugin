package software.amazon.smithy.intellij.psi

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.appendStyledSpan
import software.amazon.smithy.intellij.SmithyColorSettings
import software.amazon.smithy.intellij.SmithyJson
import software.amazon.smithy.intellij.SmithyShapeResolver.getNamespace
import software.amazon.smithy.intellij.generateLink
import software.amazon.smithy.intellij.index.SmithyDefinedShapeIdIndex.Companion.exists
import java.math.BigDecimal

/**
 * A [node value](https://awslabs.github.io/smithy/1.0/spec/core/model.html#node-values) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyValue
 * @see SmithyAstValue
 * @see SmithySyntheticValue
 */
interface SmithyValueDefinition : SmithyElement {
    val type: SmithyValueType
    val values: List<@JvmWildcard SmithyValueDefinition> get() = emptyList()
    val fields: Map<String, @JvmWildcard SmithyValueDefinition> get() = emptyMap()
    fun asBoolean(): Boolean? = null
    fun asNumber(): BigDecimal? = null
    fun asString(): String? = null
    fun equivalentTo(other: SmithyValueDefinition): Boolean = type == other.type && when (type) {
        SmithyValueType.ARRAY -> {
            val values = values
            val otherValues = other.values
            values.size == otherValues.size && values.indices.all {
                values[it].equivalentTo(otherValues[it])
            }
        }
        SmithyValueType.BOOLEAN -> asBoolean() == other.asBoolean()
        SmithyValueType.NULL -> true
        SmithyValueType.NUMBER -> asNumber() == other.asNumber()
        SmithyValueType.OBJECT -> {
            val fields = fields
            val otherFields = other.fields
            values.size == otherFields.size && fields.entries.all { (key, value) ->
                val otherValue = otherFields[key]
                otherValue != null && value.equivalentTo(otherValue)
            }
        }
        SmithyValueType.STRING -> asString() == other.asString()
    }

    fun toDocString(): String = buildString {
        when (type) {
            SmithyValueType.ARRAY -> {
                append("[")
                values.onEachIndexed { i, value ->
                    if (i != 0) append(", ")
                    append(value.toDocString())
                }
                append("]")
            }
            SmithyValueType.BOOLEAN -> appendStyledSpan(this, SmithyColorSettings.KEYWORD, asBoolean().toString(), 1f)
            SmithyValueType.NULL -> appendStyledSpan(this, SmithyColorSettings.KEYWORD, "null", 1f)
            SmithyValueType.NUMBER -> appendStyledSpan(
                this,
                SmithyColorSettings.NUMBER,
                asNumber()?.toPlainString() ?: "NaN",
                1f
            )
            SmithyValueType.OBJECT -> {
                append("{")
                fields.onEachIndexed { i, (key, value) ->
                    if (i != 0) append(", ")
                    appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, key, 1f)
                    append(": ")
                    append(value.toDocString())
                }
                append("}")
            }
            SmithyValueType.STRING -> {
                val value = asString()
                if (value != null && "#" in value) {
                    val (namespace, relativeId) = value.split("#", limit = 2)
                    if ("$" in relativeId) {
                        val (shapeName) = relativeId.split("$", limit = 2)
                        if (exists(namespace, shapeName, resolveScope)) {
                            append(
                                generateLink(
                                    value,
                                    if (getNamespace(shapeName, containingFile) != namespace) value else relativeId
                                )
                            )
                            return@buildString
                        }
                    } else if (exists(namespace, relativeId, resolveScope)) {
                        append(
                            generateLink(
                                value,
                                if (getNamespace(relativeId, containingFile) != namespace) value else relativeId
                            )
                        )
                        return@buildString
                    }
                }
                appendStyledSpan(
                    this,
                    SmithyColorSettings.STRING,
                    SmithyJson.writeValueAsString(value),
                    1f
                )
            }
        }
    }
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
