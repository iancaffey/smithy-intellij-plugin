package software.amazon.smithy.intellij

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [ParserDefinition] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyLexer
 * @see SmithyParser
 */
class SmithyParserDefinition : ParserDefinition {
    companion object {
        private val FILE = IFileElementType(SmithyLanguage)
        private val COMMENTS = TokenSet.create(SmithyTypes.TOKEN_LINE_COMMENT)
        private val STRING_LITERALS = TokenSet.create(SmithyTypes.TOKEN_STRING, SmithyTypes.TOKEN_TEXT_BLOCK)
    }

    override fun createLexer(project: Project?) = SmithyLexer()
    override fun createParser(project: Project?) = SmithyParser()
    override fun getFileNodeType() = FILE
    override fun getCommentTokens() = COMMENTS
    override fun getStringLiteralElements() = STRING_LITERALS
    override fun createElement(node: ASTNode?): PsiElement = SmithyTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider) = SmithyFile(viewProvider)
}