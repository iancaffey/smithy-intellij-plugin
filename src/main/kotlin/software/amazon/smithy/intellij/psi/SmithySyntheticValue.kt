package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiElement
import java.math.BigDecimal

/**
 * A synthetic [SmithyValueDefinition] (mostly used for annotation traits having an implicit empty object as well as merged trait values).
 *
 * @author Ian Caffey
 * @since 1.0
 */
sealed class SmithySyntheticValue : SmithySyntheticElement(), SmithyValueDefinition {
    companion object {
        fun from(value: SmithyValueDefinition): SmithySyntheticValue =
            if (value is SmithySyntheticValue) value else when (value.type) {
                SmithyValueType.ARRAY -> Array(value.values.map { from(it) })
                SmithyValueType.BOOLEAN -> Boolean(value.asBoolean())
                SmithyValueType.NULL -> Null()
                SmithyValueType.NUMBER -> Number(value.asNumber())
                SmithyValueType.OBJECT -> Object(value.fields.mapValues { (_, value) -> from(value) })
                SmithyValueType.STRING -> String(value.asString())
            }
    }

    abstract fun scope(parent: PsiElement)

    data class Array(override val values: List<SmithySyntheticValue>) : SmithySyntheticValue() {
        private lateinit var enclosing: PsiElement
        override val type = SmithyValueType.ARRAY
        override fun getParent() = enclosing
        override fun toString() = values.toString()
        override fun scope(parent: PsiElement) {
            enclosing = parent
            values.forEach { it.scope(this) }
        }
    }

    data class Boolean(val value: kotlin.Boolean?) : SmithySyntheticValue() {
        private lateinit var enclosing: PsiElement
        override val type = SmithyValueType.BOOLEAN
        override fun getParent() = enclosing
        override fun asBoolean() = value
        override fun toString() = value.toString()
        override fun scope(parent: PsiElement) {
            enclosing = parent
        }
    }

    data class Number(val value: BigDecimal?) : SmithySyntheticValue() {
        private lateinit var enclosing: PsiElement
        override val type = SmithyValueType.NUMBER
        override fun getParent() = enclosing
        override fun asNumber() = value
        override fun toString(): kotlin.String = value?.toPlainString() ?: "NaN"
        override fun scope(parent: PsiElement) {
            enclosing = parent
        }
    }

    class Null : SmithySyntheticValue() {
        private lateinit var enclosing: PsiElement
        override val type = SmithyValueType.NULL
        override fun getParent() = enclosing
        override fun toString() = "null"
        override fun scope(parent: PsiElement) {
            enclosing = parent
        }
    }

    data class Object(override val fields: Map<kotlin.String, SmithySyntheticValue>) : SmithySyntheticValue() {
        constructor() : this(emptyMap())

        private lateinit var enclosing: PsiElement
        override val type = SmithyValueType.OBJECT
        override fun getParent() = enclosing
        override fun toString() = fields.toString()
        override fun scope(parent: PsiElement) {
            enclosing = parent
            fields.values.forEach { it.scope(this) }
        }
    }

    data class String(val value: kotlin.String?) : SmithySyntheticValue() {
        private lateinit var enclosing: PsiElement
        override val type = SmithyValueType.STRING
        override fun getParent() = enclosing
        override fun asString() = value
        override fun toString() = value ?: "<invalid string>"
        override fun scope(parent: PsiElement) {
            enclosing = parent
        }
    }
}