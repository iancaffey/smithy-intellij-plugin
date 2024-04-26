package software.amazon.smithy.intellij

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.spellchecker.inspections.CommentSplitter
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.inspections.Splitter
import com.intellij.spellchecker.tokenizer.PsiIdentifierOwnerTokenizer
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer
import software.amazon.smithy.intellij.psi.SmithyString
import software.amazon.smithy.intellij.psi.SmithyTextBlock

/**
 * A [SpellcheckingStrategy] for [Smithy](https://awslabs.github.io/smithy) IDL model files.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithySpellcheckingStrategy : SpellcheckingStrategy() {
    override fun getTokenizer(element: PsiElement): Tokenizer<out PsiElement> = when (element) {
        is PsiComment -> CommentTokenizer
        is PsiNameIdentifierOwner -> PsiIdentifierOwnerTokenizer()
        is SmithyString -> StringTokenizer
        is SmithyTextBlock -> TextBlockTokenizer
        else -> EMPTY_TOKENIZER
    }
}

abstract class SimpleTokenizer<T : PsiElement> : Tokenizer<T>() {
    abstract val splitter: Splitter
    abstract fun textRange(t: T): TextRange
    override fun tokenize(t: T, consumer: TokenConsumer) {
        consumer.consumeToken(t, t.text, false, 0, textRange(t), splitter)
    }
}

object CommentTokenizer : SimpleTokenizer<PsiComment>() {
    override val splitter = CommentSplitter.getInstance()
    override fun textRange(comment: PsiComment) =
        TextRange.create(if (comment is PsiDocCommentBase) 3 else 2, comment.textLength)
}

object StringTokenizer : SimpleTokenizer<SmithyString>() {
    override val splitter = PlainTextSplitter.getInstance()
    override fun textRange(string: SmithyString) = TextRange.create(1, string.textLength - 1)
}

object TextBlockTokenizer : SimpleTokenizer<SmithyTextBlock>() {
    override val splitter = PlainTextSplitter.getInstance()
    override fun textRange(text: SmithyTextBlock) = TextRange.create(3, text.textLength - 3)
}
