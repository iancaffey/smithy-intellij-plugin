package software.amazon.smithy.intellij

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.util.elementType
import com.intellij.psi.util.siblings

/**
 * A [FoldingBuilder] for [Smithy](https://awslabs.github.io/smithy).
 *
 * All tokens supported in [SmithyBraceMatcher] can be folded.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyFoldingBuilder : FoldingBuilderEx() {
    override fun isCollapsedByDefault(node: ASTNode) = false
    override fun getPlaceholderText(node: ASTNode): String {
        val pair = SmithyBraceMatcher.PAIRS.first { pair -> node.elementType == pair.leftBraceType }
        return "${pair.leftBraceType}...${pair.rightBraceType}"
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean) =
        SyntaxTraverser.psiTraverser(root).filterTypes {
            SmithyBraceMatcher.PAIRS.any { pair -> it == pair.leftBraceType }
        }.toList().mapNotNull {
            val pair = SmithyBraceMatcher.PAIRS.first { pair -> it.elementType == pair.leftBraceType }
            it.siblings().lastOrNull { sibling -> sibling.elementType == pair.rightBraceType }?.let { closingBrace ->
                FoldingDescriptor(it.node, it.textRange.union(closingBrace.textRange))
            }
        }.toTypedArray()
}