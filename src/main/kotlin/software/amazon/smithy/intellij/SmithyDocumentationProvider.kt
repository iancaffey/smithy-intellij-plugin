package software.amazon.smithy.intellij

import com.intellij.codeInsight.documentation.DocumentationManagerUtil.createHyperlink
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.appendStyledSpan
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.getStyledSpan
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil.findChildrenOfType
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import software.amazon.smithy.intellij.index.SmithyAstShapeIndex
import software.amazon.smithy.intellij.index.SmithyDefinedShapeIdIndex
import software.amazon.smithy.intellij.psi.SmithyControl
import software.amazon.smithy.intellij.psi.SmithyDocumentation
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyOperationInput
import software.amazon.smithy.intellij.psi.SmithyOperationOutput
import software.amazon.smithy.intellij.psi.SmithyResourceIdentifierDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyTraitDefinition
import java.util.function.Consumer

private val linkAttributes = TextAttributes().apply {
    foregroundColor = HighlighterColors.TEXT.defaultAttributes.foregroundColor
}

fun generateLink(href: String, label: String, key: TextAttributesKey? = null) = buildString {
    createHyperlink(
        this,
        href,
        if (key != null) getStyledSpan(key, label, 1f) else getStyledSpan(linkAttributes, label, 1f),
        true
    )
}

/**
 * A [DocumentationProvider] for [Smithy](https://awslabs.github.io/smithy).
 *
 * Documentation will be converted from [CommonMark](https://spec.commonmark.org/) to HTML when rendered.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyDocumentationProvider : AbstractDocumentationProvider() {
    companion object {
        //Note: these traits have special handling in generateDoc(), so they're filtered out when displaying traits
        private val quickNavigateIgnoredTraitNames = setOf(
            "documentation", "externalDocumentation", "default", "enumValue"
        )
    }

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? = when (element) {
        is SmithyMemberDefinition -> buildString {
            element.traits.forEach {
                if (it.shapeName !in quickNavigateIgnoredTraitNames || it.resolvedNamespace != "smithy.api") {
                    append(getQuickNavigateInfo(it, it)).append("<br/>")
                }
            }
            when (val enclosingShapeType = element.enclosingShape.type) {
                "enum", "intEnum" -> {
                    appendStyledSpan(this, SmithyColorSettings.KEYWORD, "$enclosingShapeType member", 1f)
                    append(" ")
                    appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
                    element.enumValue?.let { append(" = ").append(it.toDocString()) }
                }
                else -> {
                    appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
                    element.resolvedTarget?.let { target -> append(": ${target.href}") }
                    element.defaultValue?.let { append(" = ").append(it.toDocString()) }
                }
            }
        }
        is SmithyOperationInput -> buildString {
            appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            (element.shape?.href ?: element.shapeId?.href)?.let { append(": $it") }
        }
        is SmithyOperationOutput -> buildString {
            appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            (element.shape?.href ?: element.shapeId?.href)?.let { append(": $it") }
        }
        is SmithyResourceIdentifierDefinition -> buildString {
            appendStyledSpan(this, SmithyColorSettings.SHAPE_MEMBER, element.name, 1f)
            append(": ${element.resolvedTarget.href}")
        }
        is SmithyShapeDefinition -> buildString {
            element.traits.forEach {
                if (it.shapeName !in quickNavigateIgnoredTraitNames || it.resolvedNamespace != "smithy.api") {
                    append(getQuickNavigateInfo(it, it)).append("<br/>")
                }
            }
            appendStyledSpan(this, SmithyColorSettings.KEYWORD, element.type, 1f)
            append(" ").append(element.href)
            element.mixins.takeIf { it.isNotEmpty() }?.let { mixins ->
                appendLine()
                appendStyledSpan(this, SmithyColorSettings.KEYWORD, "with", 1f)
                append(" [")
                mixins.onEachIndexed { index, mixin ->
                    if (index != 0) append(", ")
                    append(mixin.href)
                }
                append("]")
            }
        }
        is SmithyTraitDefinition -> element.toDocString()
        is SmithyControl -> when (element.name) {
            "version" -> "Defines the version of the Smithy IDL to use for a model file."
            "operationInputSuffix" -> "Defines the suffix to use for inline operation input shapes."
            "operationOutputSuffix" -> "Defines the suffix to use for inline operation output shapes."
            else -> null
        }?.let { renderDocumentation(it) }
        else -> null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?) = when (element) {
        is SmithyMemberDefinition -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val docs = element.findTrait("smithy.api", "documentation")?.value?.asString()?.let {
                renderDocumentation(it)
            }
            docs?.let { append("<div class='content'>").append(docs).append("</div>") }
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.href
            )
            element.resolve()?.let { target ->
                additionalInfo["Type"] = getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            element.defaultValue?.let { additionalInfo["Default value"] = it.toDocString() }
            element.enumValue?.let { additionalInfo["Value"] = it.toDocString() }
            element.findTrait("smithy.api", "externalDocumentation")?.let {
                additionalInfo["See also"] = it.value.fields.mapNotNull { (title, value) ->
                    value.asString()?.let { href -> "<a href='$href'>$title</a>" }
                }.joinToString("<span>, </span>", "<div>", "</div>")
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyOperationInput -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.href
            )
            (element.shape ?: element.shapeId?.resolve())?.let { target ->
                additionalInfo["Type"] = getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyOperationOutput -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Parent" to element.enclosingShape.href
            )
            (element.shape ?: element.shapeId?.resolve())?.let { target ->
                additionalInfo["Type"] = getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyResourceIdentifierDefinition -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val additionalInfo = mutableMapOf(
                "Namespace" to element.enclosingShape.namespace,
                "Resource" to element.enclosingShape.href
            )
            element.resolve()?.let { target ->
                additionalInfo["Type"] = getStyledSpan(SmithyColorSettings.KEYWORD, target.type, 1f)
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        is SmithyShapeDefinition -> buildString {
            append("<div class='definition'><pre>")
            append(getQuickNavigateInfo(element, originalElement))
            append("</pre></div>")
            val docs = element.findTrait("smithy.api", "documentation")?.value?.asString()?.let {
                renderDocumentation(it)
            }
            docs?.let { append("<div class='content'>").append(docs).append("</div>") }
            val additionalInfo = mutableMapOf("Namespace" to element.namespace)
            element.findTrait("smithy.api", "externalDocumentation")?.let {
                additionalInfo["See also"] = it.value.fields.mapNotNull { (title, value) ->
                    value.asString()?.let { href -> "<a href='$href'>$title</a>" }
                }.joinToString("<span>, </span>", "<div>", "</div>")
            }
            append(renderAdditionalInfo(additionalInfo))
        }
        else -> getQuickNavigateInfo(element, originalElement)
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager, link: String, context: PsiElement
    ): PsiElement? {
        if ("#" !in link) return null
        val namespace: String
        val shapeName: String
        val memberName: String?
        if ("$" in link) {
            val parts = link.split("#", limit = 2)
            namespace = parts[0]
            val memberParts = parts[1].split("$", limit = 2)
            shapeName = memberParts[0]
            memberName = memberParts[1]
        } else {
            val parts = link.split("#", limit = 2)
            namespace = parts[0]
            shapeName = parts[1]
            memberName = null
        }
        val enclosingFile =
            SmithyDefinedShapeIdIndex.getFiles(namespace, shapeName, context.resolveScope).firstOrNull()?.let {
                psiManager.findFile(it)
            } ?: return null
        if (enclosingFile !is SmithyFile) {
            return SmithyAstShapeIndex.getShapes(namespace, shapeName, context.resolveScope).firstOrNull()
                ?: enclosingFile
        }
        val shape = (enclosingFile as? SmithyFile)?.model?.shapes?.find { it.shapeName == shapeName }
        if (shape == null || memberName == null) return shape
        return shape.members.find { it.name == memberName } ?: shape
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase) =
        (comment as? SmithyDocumentation)?.let { renderDocumentation(it.toDocString()) }

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
