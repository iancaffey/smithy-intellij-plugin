package software.amazon.smithy.intellij.psi

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.impl.FakePsiElement
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyColorSettings
import software.amazon.smithy.intellij.SmithyLanguage

/**
 * A [trait](https://awslabs.github.io/smithy/1.0/spec/core/model.html#traits) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstTrait(
    val target: SmithyDefinition, override val shapeId: String, val body: Any
) : FakePsiElement(), SmithyTraitDefinition {
    override val shapeName = shapeId.split('#', limit = 2)[1]
    override fun getParent() = target
    override fun toDocString() = buildString {
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
    }
}
