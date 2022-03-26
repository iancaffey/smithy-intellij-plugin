package software.amazon.smithy.intellij

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import software.amazon.smithy.intellij.psi.SmithyDocumentation
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyTrait
import java.util.function.Consumer

/**
 * A [DocumentationProvider] for [Smithy](https://awslabs.github.io/smithy).
 *
 * [SmithyDocumentation] will be converted from [CommonMark](https://spec.commonmark.org/) to HTML when rendered.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyDocumentationProvider : AbstractDocumentationProvider() {
    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? = when (element) {
        is SmithyMember -> buildString {
            element.declaredTraits.forEach { append(getQuickNavigateInfo(it, it)).append("<br/>") }
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            append(": ${element.shapeId.text}")
        }
        is SmithyShape -> buildString {
            element.declaredTraits.forEach { append(getQuickNavigateInfo(it, it)).append("<br/>") }
            //Note: the previous non-whitespace element is the "type" (e.g. service, operation, etc.)
            val type = generateSequence(element.nameIdentifier.prevSibling) { it.prevSibling }.first {
                it !is PsiWhiteSpace && it !is PsiComment
            }
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.KEYWORD, type.text, 1f)
            append(" ").append(element.name)
        }
        is SmithyTrait -> buildString {
            //Note: since this can only do lexer-based highlighting, this will be approximately the same style as
            //the editor display (but could have some keyword highlighting issues in the body)
            HtmlSyntaxInfoUtil.appendStyledSpan(
                this,
                SmithyColorSettings.TRAIT_NAME,
                "@${element.shapeId.text}",
                1f
            )
            element.body?.let { body ->
                //Note: since keys are dynamically highlighted using an annotator, this will manually apply the same
                //behavior to improve readability of trait values
                if (body.values.isNotEmpty()) {
                    append(body.values.joinToString(", ", "(", ")") {
                        val key = HtmlSyntaxInfoUtil.getStyledSpan(
                            SmithyColorSettings.KEY,
                            it.key.text,
                            1f
                        )
                        val value = HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                            element.project,
                            SmithyLanguage,
                            it.value.text,
                            1f
                        )
                        "$key: $value"
                    })
                } else {
                    HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                        this,
                        element.project,
                        SmithyLanguage,
                        body.text,
                        1f
                    )
                }
            }
        }
        else -> null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?) = when (element) {
        is SmithyMember -> buildString {
            element.documentation?.let { append(generateRenderedDoc(it)).append("\n") }
            append(getQuickNavigateInfo(element, originalElement))
        }
        is SmithyShape -> buildString {
            element.documentation?.let { append(generateRenderedDoc(it)).append("\n") }
            append(getQuickNavigateInfo(element, originalElement))
        }
        else -> null
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        val doc = comment as? SmithyDocumentation ?: return null
        val text = doc.toDocString()
        val descriptor = CommonMarkFlavourDescriptor()
        val markdown = MarkdownParser(descriptor).buildMarkdownTreeFromString(text)
        return HtmlGenerator(text, markdown, descriptor).generateHtml()
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        PsiTreeUtil.findChildrenOfType(file, SmithyDocumentation::class.java).forEach(sink)
    }
}
