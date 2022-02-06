package software.amazon.smithy.intellij

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.SyntaxTraverser
import com.intellij.util.containers.map2Array
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [FoldingBuilder] for [Smithy](https://awslabs.github.io/smithy).
 *
 * All elements which are enclosed by curly braces will support folding.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyFoldingBuilder : FoldingBuilderEx() {
    override fun isCollapsedByDefault(node: ASTNode) = false
    override fun getPlaceholderText(node: ASTNode) = "{...}"
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean) =
        SyntaxTraverser.psiTraverser(root).filterTypes { it == SmithyTypes.TOKEN_OPEN_BRACE }.toList()
            .mapNotNull { it.parent }.map2Array { FoldingDescriptor(it.node, it.textRange) }
}