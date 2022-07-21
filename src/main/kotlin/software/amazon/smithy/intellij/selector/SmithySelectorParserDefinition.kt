package software.amazon.smithy.intellij.selector

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import software.amazon.smithy.intellij.SmithyLexer
import software.amazon.smithy.intellij.selector.psi.SmithySelectorTypes

/**
 * A [ParserDefinition] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyLexer
 * @see SmithyParser
 */
class SmithySelectorParserDefinition : ParserDefinition {
    companion object {
        private val FILE = IFileElementType(SmithySelectorLanguage)
        private val STRING_LITERALS = TokenSet.create(SmithySelectorTypes.TOKEN_STRING)
    }

    override fun createLexer(project: Project?) = SmithySelectorLexer()
    override fun createParser(project: Project?) = SmithySelectorParser()
    override fun getFileNodeType() = FILE
    override fun getCommentTokens(): TokenSet = TokenSet.EMPTY
    override fun getStringLiteralElements() = STRING_LITERALS
    override fun createElement(node: ASTNode?): PsiElement = SmithySelectorTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider) = TODO()
}