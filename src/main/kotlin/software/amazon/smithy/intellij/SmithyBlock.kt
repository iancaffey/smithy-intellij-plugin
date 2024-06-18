package software.amazon.smithy.intellij

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import software.amazon.smithy.intellij.psi.SmithyContainer
import software.amazon.smithy.intellij.psi.SmithyElement
import software.amazon.smithy.intellij.psi.SmithyModel
import software.amazon.smithy.intellij.psi.SmithyTraitBody

/**
 * A formatter [Block] for [Smithy](https://awslabs.github.io/smithy).
 *
 * [SmithyFormattingModelBuilder] configures the [SpacingBuilder] to handle the majority of [SmithyModel] formatting.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyBlock(
    node: ASTNode,
    private val spacingBuilder: SpacingBuilder,
    private val parent: SmithyBlock? = null,
    private val indent: Indent = Indent.getNoneIndent(),
    wrap: Wrap? = Wrap.createWrap(WrapType.NONE, false),
    alignment: Alignment? = if (parent != null) parent.alignment else Alignment.createAlignment(),
) : AbstractBlock(node, wrap, alignment) {

    private val childAlignment = Alignment.createAlignment()
    private val childIndent = if (node.psi is SmithyContainer) Indent.getNormalIndent() else Indent.getNoneIndent()
    private val childWrap = Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true)

    override fun isLeaf() = node.firstChildNode !is SmithyElement
    override fun getIndent() = indent
    override fun getChildAttributes(newChildIndex: Int) = ChildAttributes(childIndent, childAlignment)
    override fun getChildIndent(): Indent = childIndent
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)
    override fun buildChildren(): List<SmithyBlock> {
        val children = node.getChildren(TokenSet.forAllMatching { it != TokenType.WHITE_SPACE })
        return children.map {
            //Note: all children elements (besides top-level containers within trait body) will be indented
            //      Trait body containers are not indented as it would seem to doubly indent elements
            val requiresIndent = node.psi is SmithyContainer && it.psi.let { psi ->
                psi is PsiComment || psi is SmithyElement
            } && !(node.psi is SmithyTraitBody && it.psi is SmithyContainer)
            //Note: closing braces are wrapped alongside other children but take on the indent/alignment of the parent
            val closingBrace = node.psi is SmithyContainer && SmithyBraceMatcher.PAIRS.any { pair ->
                it.elementType == pair.rightBraceType
            }
            if (requiresIndent) SmithyBlock(it, spacingBuilder, this, childIndent, childWrap, alignment(children, it))
            else if (closingBrace) SmithyBlock(it, spacingBuilder, this, wrap = childWrap)
            else SmithyBlock(it, spacingBuilder, this)
        }
    }

    private fun alignment(children: Array<ASTNode>, current: ASTNode): Alignment? {
        val inherit = current.psi is SmithyContainer
                && node.psi is SmithyContainer
                && children.count { c -> c.psi is SmithyContainer } == 1
                && onSameLines(node, current)
        return if (inherit) alignment else childAlignment
    }

    private fun onSameLines(parent: ASTNode, child: ASTNode): Boolean {
        val documentManager = PsiDocumentManager.getInstance(node.psi.project)
        val document = documentManager.getDocument(node.psi.containingFile) ?: return false
        return onSameLine(document, parent.startOffset, child.startOffset)
                && onSameLine(document, parent.startOffset + parent.textLength, child.startOffset + child.textLength)
    }

    private fun onSameLine(document: Document, first: Int, second: Int): Boolean {
        return document.getLineNumber(first) == document.getLineNumber(second)
    }
}
