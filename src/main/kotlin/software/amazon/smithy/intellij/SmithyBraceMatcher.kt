package software.amazon.smithy.intellij

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [PairedBraceMatcher] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyBraceMatcher : PairedBraceMatcher {
    companion object {
        private val PAIRS = arrayOf(
            BracePair(SmithyTypes.TOKEN_OPEN_BRACE, SmithyTypes.TOKEN_CLOSE_BRACE, true),
            BracePair(SmithyTypes.TOKEN_OPEN_BRACKET, SmithyTypes.TOKEN_CLOSE_BRACKET, false),
            BracePair(SmithyTypes.TOKEN_OPEN_PAREN, SmithyTypes.TOKEN_CLOSE_PAREN, false)
        )
    }

    override fun getPairs() = PAIRS
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?) = true
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset
}