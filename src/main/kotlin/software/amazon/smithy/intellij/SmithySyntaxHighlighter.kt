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
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiComment
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
            SmithyTypes.METADATA_DEFINITION,
            SmithyTypes.NAMESPACE_DEFINITION,
            SmithyTypes.SHAPE_DEFINITION
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is SmithyTraitName) {
            holder.assign(SmithyColorSettingsPage.TRAIT_NAME)
        }
        //All control statement keys (currently only version) are colored as keywords since they are parser directives
        if ((element is SmithyKey || element.elementType == SmithyTypes.TOKEN_DOLLAR_SIGN) && element.parent is SmithyControlDefinition) {
            holder.assign(SmithyColorSettingsPage.KEYWORD)
        }
        //Reset all strings used as keys back to the normal text color
        if (element is SmithyString && element.parent is SmithyKey) {
            holder.assign(HighlighterColors.TEXT)
        }
        //Reset all keywords/literals/types used as identifiers back to the normal text color
        if ((element is SmithyBoolean || element is SmithyKeyword || element is SmithyNull || element is SmithySimpleTypeName) && element.parent is SmithyId) {
            holder.assign(HighlighterColors.TEXT)
        }
        if (element is PsiComment && element.text.startsWith("///")) {
            holder.assign(SmithyColorSettingsPage.DOC_COMMENT)
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

    private fun AnnotationHolder.assign(key: TextAttributesKey) =
        newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(key).create()

    private fun AnnotationHolder.highlight(severity: HighlightSeverity, message: String) =
        newAnnotation(severity, message).create()
}

/**
 * A [SyntaxHighlighter] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyColorSettingsPage
 */
class SmithySyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        //Note: these can only match against tokens, SmithyAnnotator supports the contextual syntax highlighting with the higher-level AST nodes
        private val TOKEN_HIGHLIGHTS = mapOf(
            SmithyTypes.TOKEN_NUMBER to arrayOf(SmithyColorSettingsPage.NUMBER),
            SmithyTypes.TOKEN_STRING to arrayOf(SmithyColorSettingsPage.STRING),
            SmithyTypes.TOKEN_TEXT_BLOCK to arrayOf(SmithyColorSettingsPage.STRING),
            SmithyTypes.TOKEN_INCOMPLETE_STRING to arrayOf(SmithyColorSettingsPage.STRING),
            SmithyTypes.TOKEN_INCOMPLETE_TEXT_BLOCK to arrayOf(SmithyColorSettingsPage.STRING),
            SmithyTypes.TOKEN_COMMENT to arrayOf(SmithyColorSettingsPage.LINE_COMMENT),
            SmithyTypes.TOKEN_OPEN_BRACE to arrayOf(SmithyColorSettingsPage.BRACES),
            SmithyTypes.TOKEN_CLOSE_BRACE to arrayOf(SmithyColorSettingsPage.BRACES),
            SmithyTypes.TOKEN_OPEN_BRACKET to arrayOf(SmithyColorSettingsPage.BRACKETS),
            SmithyTypes.TOKEN_CLOSE_BRACKET to arrayOf(SmithyColorSettingsPage.BRACKETS),
            SmithyTypes.TOKEN_OPEN_PAREN to arrayOf(SmithyColorSettingsPage.PARENS),
            SmithyTypes.TOKEN_CLOSE_PAREN to arrayOf(SmithyColorSettingsPage.PARENS),
            SmithyTypes.TOKEN_COMMA to arrayOf(SmithyColorSettingsPage.COMMA),
            SmithyTypes.TOKEN_PERIOD to arrayOf(SmithyColorSettingsPage.DOT),
            SmithyTypes.TOKEN_SIMPLE_TYPE_NAME to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_BOOLEAN to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_NULL to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_APPLY to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_LIST to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_MAP to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_OPERATION to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_METADATA to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_NAMESPACE to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_RESOURCE to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_SERVICE to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_SET to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_STRUCTURE to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_UNION to arrayOf(SmithyColorSettingsPage.KEYWORD),
            SmithyTypes.TOKEN_USE to arrayOf(SmithyColorSettingsPage.KEYWORD)
        )
    }

    override fun getHighlightingLexer() = SmithyLexer()
    override fun getTokenHighlights(tokenType: IElementType?) = TOKEN_HIGHLIGHTS[tokenType] ?: emptyArray()
}