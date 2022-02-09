package software.amazon.smithy.intellij

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [CompletionContributor] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyCompletionContributor : CompletionContributor() {
    companion object {
        private val CONTROL = setOf(
            "version"
        )
        private val KEYWORDS = setOf(
            SmithyTypes.TOKEN_APPLY,
            SmithyTypes.TOKEN_LIST,
            SmithyTypes.TOKEN_MAP,
            SmithyTypes.TOKEN_METADATA,
            SmithyTypes.TOKEN_NAMESPACE,
            SmithyTypes.TOKEN_NULL,
            SmithyTypes.TOKEN_OPERATION,
            SmithyTypes.TOKEN_RESOURCE,
            SmithyTypes.TOKEN_SERVICE,
            SmithyTypes.TOKEN_SET,
            SmithyTypes.TOKEN_STRUCTURE,
            SmithyTypes.TOKEN_UNION,
            SmithyTypes.TOKEN_USE
        ).map { it.toString() }
        private val PRELUDE_TYPES = setOf(
            "blob",
            "boolean",
            "document",
            "string",
            "byte",
            "short",
            "integer",
            "long",
            "float",
            "double",
            "bigInteger",
            "bigDecimal",
            "timestamp"
        )
        private val PRELUDE_SHAPES = setOf(
            "Blob",
            "Boolean",
            "Document",
            "String",
            "Byte",
            "Short",
            "Integer",
            "Long",
            "Float",
            "Double",
            "BigInteger",
            "BigDecimal",
            "Timestamp",
            "PrimitiveBoolean",
            "PrimitiveByte",
            "PrimitiveShort",
            "PrimitiveInteger",
            "PrimitiveLong",
            "PrimitiveFloat",
            "PrimitiveDouble",
            "Unit"
        )
        private val GLOBAL_SYMBOLS = arrayOf(CONTROL, KEYWORDS, PRELUDE_SHAPES, PRELUDE_TYPES).flatMap { it }
    }

    init {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement(SmithyTypes.TOKEN_SYMBOL),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {
                    GLOBAL_SYMBOLS.forEach { result.addElement(LookupElementBuilder.create(it)) }
                }
            })
    }
}