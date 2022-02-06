package software.amazon.smithy.intellij

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import software.amazon.smithy.intellij.psi.*

/**
 * A [SyntaxHighlighterFactory] for [SmithySyntaxHighlighter].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithySyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) = SmithySyntaxHighlighter()
}

/**
 * An [Annotator] which completes the syntax highlighting which requires context provided by higher-level AST nodes not provided to [SmithySyntaxHighlighter].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithySyntaxAnnotator : Annotator {
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

/**
 * A [SyntaxHighlighter] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyColorSettings
 */
class SmithySyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        //Note: these can only match against tokens, SmithySyntaxAnnotator supports the contextual syntax highlighting with the higher-level AST nodes
        private val TOKEN_HIGHLIGHTS = mapOf(
            SmithyTypes.TOKEN_NUMBER to arrayOf(SmithyColorSettings.NUMBER),
            SmithyTypes.TOKEN_STRING to arrayOf(SmithyColorSettings.STRING),
            SmithyTypes.TOKEN_TEXT_BLOCK to arrayOf(SmithyColorSettings.STRING),
            SmithyTypes.TOKEN_INCOMPLETE_STRING to arrayOf(SmithyColorSettings.STRING),
            SmithyTypes.TOKEN_INCOMPLETE_TEXT_BLOCK to arrayOf(SmithyColorSettings.STRING),
            SmithyTypes.TOKEN_LINE_COMMENT to arrayOf(SmithyColorSettings.LINE_COMMENT),
            SmithyTypes.TOKEN_DOCUMENTATION_LINE to arrayOf(SmithyColorSettings.DOC_COMMENT),
            SmithyTypes.TOKEN_OPEN_BRACE to arrayOf(SmithyColorSettings.BRACES),
            SmithyTypes.TOKEN_CLOSE_BRACE to arrayOf(SmithyColorSettings.BRACES),
            SmithyTypes.TOKEN_OPEN_BRACKET to arrayOf(SmithyColorSettings.BRACKETS),
            SmithyTypes.TOKEN_CLOSE_BRACKET to arrayOf(SmithyColorSettings.BRACKETS),
            SmithyTypes.TOKEN_OPEN_PAREN to arrayOf(SmithyColorSettings.PARENS),
            SmithyTypes.TOKEN_CLOSE_PAREN to arrayOf(SmithyColorSettings.PARENS),
            SmithyTypes.TOKEN_COMMA to arrayOf(SmithyColorSettings.COMMA),
            SmithyTypes.TOKEN_PERIOD to arrayOf(SmithyColorSettings.DOT),
            SmithyTypes.TOKEN_SIMPLE_TYPE_NAME to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_BOOLEAN to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_NULL to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_APPLY to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_LIST to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_MAP to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_OPERATION to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_METADATA to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_NAMESPACE to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_RESOURCE to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_SERVICE to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_SET to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_STRUCTURE to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_UNION to arrayOf(SmithyColorSettings.KEYWORD),
            SmithyTypes.TOKEN_USE to arrayOf(SmithyColorSettings.KEYWORD)
        )
    }

    override fun getHighlightingLexer() = SmithyLexer()
    override fun getTokenHighlights(tokenType: IElementType?) = TOKEN_HIGHLIGHTS[tokenType] ?: emptyArray()
}