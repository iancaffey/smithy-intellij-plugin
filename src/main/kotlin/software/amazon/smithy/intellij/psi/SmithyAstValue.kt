package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.SmithyAst.Value

/**
 * A [node value](https://awslabs.github.io/smithy/1.0/spec/core/model.html#node-values) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).

 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstValue(
    val enclosing: PsiElement, val value: Value
) : SmithySyntheticElement(), SmithyValueDefinition {
    override val type = when (value) {
        is Value.Array -> SmithyValueType.ARRAY
        is Value.Boolean -> SmithyValueType.BOOLEAN
        is Value.Null -> SmithyValueType.NULL
        is Value.Number -> SmithyValueType.NUMBER
        is Value.Object -> SmithyValueType.OBJECT
        is Value.String -> SmithyValueType.STRING
    }
    override val values: List<SmithyAstValue> = (value as? Value.Array)?.let {
        it.values.map { value -> SmithyAstValue(this, value) }
    } ?: emptyList()
    override val fields: Map<String, SmithyAstValue> = (value as? Value.Object)?.let {
        it.fields.entries.associate { (key, value) -> key to SmithyAstValue(this, value) }
    } ?: emptyMap()

    override fun getParent() = enclosing
    override fun asBoolean() = (value as? Value.Boolean)?.value
    override fun asNumber() = (value as? Value.Number)?.value
    override fun asString() = (value as? Value.String)?.value
    override fun toString() = value.toString()
}
