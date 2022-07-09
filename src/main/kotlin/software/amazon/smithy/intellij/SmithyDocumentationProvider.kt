package software.amazon.smithy.intellij

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import software.amazon.smithy.intellij.psi.SmithyAstMember
import software.amazon.smithy.intellij.psi.SmithyAstShape
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
    companion object {
        private val DOCUMENTATION = "smithy.api#documentation"
        private val EXTERNAL_DOCUMENTATION = "smithy.api#externalDocumentation"
        private val DOCUMENTATION_TRAITS = setOf(DOCUMENTATION, EXTERNAL_DOCUMENTATION)
    }

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? = when (element) {
        is SmithyMember -> buildString {
            element.declaredTraits.forEach { append(getQuickNavigateInfo(it, it)).append("<br/>") }
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            append(": ${element.shapeId.text}")
        }
        is SmithyShape -> buildString {
            element.declaredTraits.forEach { append(getQuickNavigateInfo(it, it)).append("<br/>") }
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.KEYWORD, element.type, 1f)
            append(" ").append(element.name)
        }
        is SmithyTrait -> buildString {
            //Note: since this can only do lexer-based highlighting, this will be approximately the same style as
            //the editor display (but could have some keyword highlighting issues in the body)
            HtmlSyntaxInfoUtil.appendStyledSpan(
                this, SmithyColorSettings.TRAIT_NAME, "@${element.shapeId.text}", 1f
            )
            element.body?.let { body ->
                //Note: since keys are dynamically highlighted using an annotator, this will manually apply the same
                //behavior to improve readability of trait values
                if (body.values.isNotEmpty()) {
                    append(body.values.joinToString(", ", "(", ")") {
                        val key = HtmlSyntaxInfoUtil.getStyledSpan(
                            SmithyColorSettings.KEY, it.key.text, 1f
                        )
                        val value = HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                            element.project, SmithyLanguage, it.value.text, 1f
                        )
                        "$key: $value"
                    })
                } else {
                    HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                        this, element.project, SmithyLanguage, body.text, 1f
                    )
                }
            }
        }
        is SmithyAstMember -> buildString {
            append(renderTraitDocumentation(element.project, element.reference.traits))
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            append(": ${element.reference.target.split('#', limit = 2)[1]}")
        }
        is SmithyAstShape -> buildString {
            append(renderTraitDocumentation(element.project, element.shape.traits))
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.KEYWORD, element.type, 1f)
            append(" ").append(element.name)
        }
        else -> null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?) = when (element) {
        is SmithyMember -> buildString {
            element.documentation?.let { append(generateRenderedDoc(it)) }
            append(getQuickNavigateInfo(element, originalElement))
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.name
            )
            element.resolve()?.let { target ->
                additionalInfo["Type"] = HtmlSyntaxInfoUtil.getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyShape -> buildString {
            element.documentation?.let { append(generateRenderedDoc(it)) }
            append(getQuickNavigateInfo(element, originalElement))
            append(renderAdditionalInfo(mapOf("Namespace" to element.namespace)))
        }
        is SmithyAstMember -> buildString {
            element.reference.traits?.get(DOCUMENTATION)
                ?.let { append(renderDocumentation(it.toString())).append("<br/>") }
            append(getQuickNavigateInfo(element, originalElement))
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.name
            )
            element.resolve()?.let { target ->
                additionalInfo["Type"] = HtmlSyntaxInfoUtil.getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            element.reference.traits?.get(EXTERNAL_DOCUMENTATION)?.let {
                additionalInfo["See also"] = (it as Map<*, *>).entries.joinToString(
                    "<span>, </span>", "<div>", "</div>"
                ) { (title, href) -> "<a href='$href'>$title</a>" }
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyAstShape -> buildString {
            element.shape.traits?.get(DOCUMENTATION)?.let { append(renderDocumentation(it.toString())).append("<br/>") }
            append(getQuickNavigateInfo(element, originalElement))
            val additionalInfo = mutableMapOf("Namespace" to element.namespace)
            element.shape.traits?.get(EXTERNAL_DOCUMENTATION)?.let {
                additionalInfo["See also"] = (it as Map<*, *>).entries.joinToString(
                    "<span>, </span>", "<div>", "</div>"
                ) { (title, href) -> "<a href='$href'>$title</a>" }
            }
            if (additionalInfo.isNotEmpty()) {
                append(renderAdditionalInfo(additionalInfo))
            }
        }
        else -> null
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase) =
        (comment as? SmithyDocumentation)?.let { renderDocumentation(it.toDocString()) }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        PsiTreeUtil.findChildrenOfType(file, SmithyDocumentation::class.java).forEach(sink)
    }

    private fun renderAdditionalInfo(info: Map<String, String>) = buildString {
        append("<table class='sections'>")
        info.forEach { (key, value) ->
            append("<tr><td valign='top' class='section'>$key: </td><td valign='top'>$value</td></tr>")
        }
        append("</table>")
    }

    private fun renderDocumentation(text: String): String {
        val descriptor = CommonMarkFlavourDescriptor()
        val markdown = MarkdownParser(descriptor).buildMarkdownTreeFromString(text)
        return HtmlGenerator(text, markdown, descriptor).generateHtml()
    }

    private fun renderTraitDocumentation(project: Project, traits: Map<String, Any>?) = buildString {
        traits?.forEach { (shapeId, body) ->
            if (shapeId in DOCUMENTATION_TRAITS) return@forEach
            HtmlSyntaxInfoUtil.appendStyledSpan(
                this, SmithyColorSettings.TRAIT_NAME, "@${shapeId.split('#', limit = 2)[1]}", 1f
            )
            if (body is Map<*, *>) {
                if (body.isNotEmpty()) {
                    append(body.entries.joinToString(", ", "(", ")") {
                        val key = HtmlSyntaxInfoUtil.getStyledSpan(
                            SmithyColorSettings.KEY, it.key as String, 1f
                        )
                        val value = HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                            project, SmithyLanguage, SmithyAst.toJson(it.value), 1f
                        )
                        "$key: $value"
                    })
                }
            } else {
                append("(")
                append(
                    HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                        project, SmithyLanguage, SmithyAst.toJson(body), 1f
                    )
                )
                append(")")
            }
            append("<br/>")
        }
    }
}
