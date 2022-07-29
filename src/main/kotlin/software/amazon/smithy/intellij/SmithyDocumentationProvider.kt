package software.amazon.smithy.intellij

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.appendStyledSpan
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.getStyledSpan
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.findChildrenOfType
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import software.amazon.smithy.intellij.psi.SmithyDocumentation
import software.amazon.smithy.intellij.psi.SmithyDocumentationDefinition
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyTraitDefinition
import java.util.function.Consumer

/**
 * A [DocumentationProvider] for [Smithy](https://awslabs.github.io/smithy).
 *
 * [SmithyDocumentationDefinition.toDocString] will be converted from [CommonMark](https://spec.commonmark.org/) to HTML when rendered.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyDocumentationProvider : AbstractDocumentationProvider() {
    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? = when (element) {
        is SmithyMemberDefinition -> buildString {
            sequenceOf(element.appliedTraits, element.declaredTraits).flatten().forEach {
                if ((it.shapeName != "documentation" && it.shapeName != "externalDocumentation") || it.resolvedNamespace != "smithy.api") {
                    append(getQuickNavigateInfo(it, it)).append("<br/>")
                }
            }
            when (val enclosingShapeType = element.enclosingShape.type) {
                "enum", "intEnum" -> {
                    appendStyledSpan(this, SmithyColorSettings.KEYWORD, "$enclosingShapeType member", 1f)
                    append(" ")
                    appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
                }
                else -> {
                    appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
                    element.resolvedTarget?.let { target -> append(": ${target.shapeName}") }
                }
            }
        }
        is SmithyShapeDefinition -> buildString {
            sequenceOf(element.appliedTraits, element.declaredTraits).flatten().forEach {
                if ((it.shapeName != "documentation" && it.shapeName != "externalDocumentation") || it.resolvedNamespace != "smithy.api") {
                    append(getQuickNavigateInfo(it, it)).append("<br/>")
                }
            }
            appendStyledSpan(this, SmithyColorSettings.KEYWORD, element.type, 1f)
            append(" ").append(element.name)
        }
        is SmithyTraitDefinition -> element.toDocString()
        else -> null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?) = when (element) {
        is SmithyMemberDefinition -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val docs = element.findTrait("smithy.api", "documentation")?.value?.asString()?.let {
                renderDocumentation(it)
            } ?: element.documentation?.let { generateRenderedDoc(it) }
            docs?.let { append("<div class='content'>").append(docs).append("</div>") }
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.name
            )
            element.resolve()?.let { target ->
                additionalInfo["Type"] = getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            element.findTrait("smithy.api", "externalDocumentation")?.let {
                additionalInfo["See also"] = it.value.fields.mapNotNull { (title, value) ->
                    value.asString()?.let { href -> "<a href='$href'>$title</a>" }
                }.joinToString("<span>, </span>", "<div>", "</div>")
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyShapeDefinition -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val docs = element.findTrait("smithy.api", "documentation")?.value?.asString()?.let {
                renderDocumentation(it)
            } ?: element.documentation?.let { generateRenderedDoc(it) }
            docs?.let { append("<div class='content'>").append(docs).append("</div>") }
            val additionalInfo = mutableMapOf("Namespace" to element.namespace)
            element.findTrait("smithy.api", "externalDocumentation")?.let {
                additionalInfo["See also"] = it.value.fields.mapNotNull { (title, value) ->
                    value.asString()?.let { href -> "<a href='$href'>$title</a>" }
                }.joinToString("<span>, </span>", "<div>", "</div>")
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        else -> null
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase) =
        (comment as? SmithyDocumentationDefinition)?.let { renderDocumentation(it.toDocString()) }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        findChildrenOfType(file, SmithyDocumentation::class.java).forEach(sink)
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
}
