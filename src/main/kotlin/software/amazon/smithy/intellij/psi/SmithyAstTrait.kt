package software.amazon.smithy.intellij.psi

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.getStyledSpan
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyColorSettings
import software.amazon.smithy.intellij.SmithyJson
import software.amazon.smithy.intellij.SmithyLanguage

/**
 * A [trait](https://awslabs.github.io/smithy/1.0/spec/core/model.html#traits) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstTrait(
    val target: SmithyDefinition, val shapeId: String, val body: SmithyAst.Value
) : SmithySyntheticElement(), SmithyTraitDefinition {
    override val value = SmithyAstValue(this, body)
    private val parts = shapeId.split('#', limit = 2)
    override val shapeName = parts[1]
    override val declaredNamespace = parts[0]
    override val resolvedNamespace = parts[0]
    override fun getName() = shapeName
    override fun getParent() = target
    override fun getPresentableText() = shapeName
    override fun getLocationString() = target.name
    override fun getIcon(unused: Boolean) = getIcon(0)
    override fun toDocString() = buildString {
        HtmlSyntaxInfoUtil.appendStyledSpan(this, SmithyColorSettings.TRAIT_NAME, "@$shapeName", 1f)
        if (value.type == SmithyValueType.OBJECT) {
            value.fields.takeIf { it.isNotEmpty() }?.let { fields ->
                append(fields.map {
                    val key = getStyledSpan(
                        SmithyColorSettings.KEY, it.key, 1f
                    )
                    val value = getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                        project, SmithyLanguage, SmithyJson.writeValueAsString(it.value), 1f
                    )
                    "$key: $value"
                }.joinToString(", ", "(", ")"))
            }
        } else {
            append("(")
            append(
                getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                    project, SmithyLanguage, SmithyJson.writeValueAsString(value), 1f
                )
            )
            append(")")
        }
    }
}
