package software.amazon.smithy.intellij

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.findChildrenOfType
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import software.amazon.smithy.intellij.SmithyPreludeShapes.DOCUMENTATION
import software.amazon.smithy.intellij.SmithyPreludeShapes.EXTERNAL_DOCUMENTATION
import software.amazon.smithy.intellij.psi.SmithyAstMember
import software.amazon.smithy.intellij.psi.SmithyAstShape
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
    companion object {
        private val DOCUMENTATION_TRAITS = setOf(DOCUMENTATION, EXTERNAL_DOCUMENTATION)
    }

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? = when (element) {
        is SmithyMemberDefinition -> buildString {
            element.declaredTraits.forEach { append(getQuickNavigateInfo(it, it)).append("<br/>") }
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            append(": ${element.targetShapeName}")
        }
        is SmithyShapeDefinition -> buildString {
            element.declaredTraits.forEach { append(getQuickNavigateInfo(it, it)).append("<br/>") }
            HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.KEYWORD, element.type, 1f)
            append(" ").append(element.name)
        }
        is SmithyTraitDefinition -> element.toDocString()
        else -> null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?) = when (element) {
        is SmithyMemberDefinition -> buildString {
            element.documentation?.let { append(generateRenderedDoc(it)) }
            append(getQuickNavigateInfo(element, originalElement))
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.name
            )
            element.resolve()?.let { target ->
                additionalInfo["Type"] = HtmlSyntaxInfoUtil.getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            //TODO: generalize this once SmithyMemberDefinition has a consolidated node value(s) API
            element.let { it as? SmithyAstMember }?.reference?.traits?.get(EXTERNAL_DOCUMENTATION)?.let {
                additionalInfo["See also"] = (it as Map<*, *>).entries.joinToString(
                    "<span>, </span>", "<div>", "</div>"
                ) { (title, href) -> "<a href='$href'>$title</a>" }
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyShapeDefinition -> buildString {
            element.documentation?.let { append(generateRenderedDoc(it)) }
            append(getQuickNavigateInfo(element, originalElement))
            val additionalInfo = mutableMapOf("Namespace" to element.namespace)
            //TODO: generalize this once SmithyShapeDefinition has a consolidated node value(s) API
            element.let { it as? SmithyAstShape }?.shape?.traits?.get(EXTERNAL_DOCUMENTATION)?.let {
                additionalInfo["See also"] = (it as Map<*, *>).entries.joinToString(
                    "<span>, </span>", "<div>", "</div>"
                ) { (title, href) -> "<a href='$href'>$title</a>" }
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
