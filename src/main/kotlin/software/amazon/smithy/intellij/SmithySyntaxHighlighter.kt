package software.amazon.smithy.intellij

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import software.amazon.smithy.intellij.psi.SmithyTypes

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