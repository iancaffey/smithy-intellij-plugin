package software.amazon.smithy.intellij

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import software.amazon.smithy.intellij.psi.*

/**
 * An [Annotator] which provides annotations to [Smithy](https://awslabs.github.io/smithy) model files.
 *
 * All syntax checks which require the higher-level AST (resulting from a complete parse) are handled to complete syntax highlighting from [SmithySyntaxHighlighter].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAnnotator : Annotator {
    companion object {
        private val TOKENS_REQUIRING_TRAILING_NEW_LINE = setOf(
            SmithyTypes.APPLY,
            SmithyTypes.CONTROL_DEFINITION,
            SmithyTypes.DOCUMENTATION,
            SmithyTypes.LIST_DEFINITION,
            SmithyTypes.MAP_DEFINITION,
            SmithyTypes.METADATA_DEFINITION,
            SmithyTypes.NAMESPACE_DEFINITION,
            SmithyTypes.OPERATION_DEFINITION,
            SmithyTypes.RESOURCE_DEFINITION,
            SmithyTypes.SERVICE_DEFINITION,
            SmithyTypes.SET_DEFINITION,
            SmithyTypes.SIMPLE_SHAPE_DEFINITION,
            SmithyTypes.STRUCTURE_DEFINITION,
            SmithyTypes.UNION_DEFINITION
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if ((element is SmithyKey || element.elementType == SmithyTypes.TOKEN_DOLLAR_SIGN) && element.parent is SmithyControlDefinition) {
            holder.highlight(SmithyColorSettings.CONTROL)
        }
        if (element is SmithyKey && element.parent !is SmithyControlDefinition) {
            holder.highlight(SmithyColorSettings.KEY)
        }
        if (element is SmithyMemberName || element is SmithyId && element.parent is SmithyShapeField) {
            holder.highlight(SmithyColorSettings.SHAPE_MEMBER)
        }
        if ((element is SmithyShapeId || element.elementType == SmithyTypes.TOKEN_AT) && element.parent is SmithyTrait) {
            holder.highlight(SmithyColorSettings.TRAIT_NAME)
        }
        //Reset all keywords/literals/types used as standalone identifiers back to the normal text color
        if ((element is SmithyBoolean || element is SmithyKeyword || element is SmithyNull || element is SmithySimpleTypeName) && element.parent is SmithyId && element.parent.parent !is SmithyKey) {
            holder.highlight(HighlighterColors.TEXT)
        }
        //Highlight all escape sequences within strings and text blocks
        if ((element is SmithyString || element is SmithyTextBlock) && element.text.contains("\\")) {
            val valid = mutableListOf<TextRange>()
            val invalid = mutableListOf<TextRange>()
            var i = 0
            while (i < element.textLength) {
                if (element.text[i] != '\\') {
                    i++
                    continue
                }
                val length = if (i < element.textLength - 5 && element.text[i + 1] == 'u' && (2..5).all {
                        val digit = element.text[i + it]
                        (digit in '0'..'9') || (digit in 'a'..'f') || (digit in 'A'..'F')
                    }) 5 else 2
                if (length == 2 && element.text[i + 1] !in "\"'bfnrt/\\\n") {
                    invalid.add(TextRange.from(i, length))
                } else {
                    valid.add(TextRange.from(i, length))
                }
                i += length
            }
            val range = element.textRange
            valid.forEach { holder.highlight(SmithyColorSettings.VALID_ESCAPE_SEQUENCE, range.cutOut(it)) }
            invalid.forEach {
                holder.highlight(
                    HighlightSeverity.ERROR,
                    "Invalid escape sequence: '${it.substring(element.text)}'",
                    SmithyColorSettings.INVALID_ESCAPE_SEQUENCE,
                    range.cutOut(it)
                )
            }
        }
        //Since whitespace cannot be referenced within parsing rules, error annotations are added for all nodes which are missing a trailing new-line (or EOF)
        if (element.elementType in TOKENS_REQUIRING_TRAILING_NEW_LINE) {
            val next = element.nextLeaf()
            //next == null -> EOF which is treated like a new-line
            if (next != null && (next !is PsiWhiteSpace || !next.text.contains("\n"))) {
                holder.highlight(HighlightSeverity.ERROR, "Expecting trailing line break '\\n'")
            }
        }
        if (element.elementType == SmithyTypes.TOKEN_INCOMPLETE_STRING) {
            holder.highlight(HighlightSeverity.ERROR, "Expecting closing quote '\"'")
        }
        if (element.elementType == SmithyTypes.TOKEN_INCOMPLETE_TEXT_BLOCK) {
            holder.highlight(HighlightSeverity.ERROR, "Expecting closing quotes '\"\"\"'")
        }
        if (element is SmithyShapeId && element.parent is SmithyTrait && element.memberName != null) {
            val expected = element.namespace?.let { "${it.name}.${element.shapeName.name}" } ?: element.shapeName.text
            holder.highlight(
                HighlightSeverity.ERROR, "Traits cannot refer to shape members. Did you mean @$expected?"
            )
        }
    }

    private fun AnnotationHolder.highlight(key: TextAttributesKey) =
        newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(key).create()

    private fun AnnotationHolder.highlight(key: TextAttributesKey, range: TextRange) =
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(range).textAttributes(key).create()

    private fun AnnotationHolder.highlight(severity: HighlightSeverity, message: String) =
        newAnnotation(severity, message).create()

    private fun AnnotationHolder.highlight(
        severity: HighlightSeverity, message: String, key: TextAttributesKey, range: TextRange
    ) = newAnnotation(severity, message).textAttributes(key).range(range).create()
}