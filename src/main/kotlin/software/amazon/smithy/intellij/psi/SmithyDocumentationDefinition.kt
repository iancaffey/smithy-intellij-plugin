package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiDocCommentBase

/**
 * A [documentation](https://awslabs.github.io/smithy/1.0/spec/core/documentation-traits.html#documentation-trait) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
interface SmithyDocumentationDefinition : PsiDocCommentBase, SmithyElement {
    fun toDocString(): String
}