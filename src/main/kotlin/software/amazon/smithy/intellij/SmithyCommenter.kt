package software.amazon.smithy.intellij

import com.intellij.lang.CodeDocumentationAwareCommenter
import com.intellij.lang.Commenter

/**
 * A [Commenter] for handling line comments in [Smithy](https://awslabs.github.io/smithy).
 *
 * [CodeDocumentationAwareCommenter] does not support the [///] documentation line prefix which Smithy uses, so all
 * prefix handling is done by [SmithyDocumentationEnterHandler].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyCommenter : Commenter {
    override fun getLineCommentPrefix() = "//"
    override fun getBlockCommentPrefix(): String? = null
    override fun getBlockCommentSuffix(): String? = null
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}