package software.amazon.smithy.intellij.psi

import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.getStyledSpan
import software.amazon.smithy.intellij.generateLink

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) target definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyAstTarget
 * @see SmithyShapeId
 * @see SmithySyntheticShapeTarget
 */
interface SmithyShapeTarget : SmithyElement {
    val href: String
        get() = buildString {
            val namespace = resolvedNamespace
            if (namespace != null)
                append(generateLink("${namespace}#${shapeName}", shapeName))
            else
                append(getStyledSpan(HighlighterColors.TEXT, shapeName, 1f))
        }
    val shapeName: String
    val declaredNamespace: String?
    val resolvedNamespace: String?
    fun resolve(): SmithyShapeDefinition?
}