package software.amazon.smithy.intellij

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import software.amazon.smithy.intellij.psi.SmithyArray
import software.amazon.smithy.intellij.psi.SmithyElement
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyModel
import software.amazon.smithy.intellij.psi.SmithyObject
import software.amazon.smithy.intellij.psi.SmithyTraitArguments
import software.amazon.smithy.intellij.psi.SmithyTraitValues
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A formatter [Block] for [Smithy](https://awslabs.github.io/smithy).
 *
 * [SmithyFormattingModelBuilder] configures the [SpacingBuilder] to handle the majority of [SmithyModel] formatting.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyBlock private constructor(
    node: ASTNode,
    private val spacingBuilder: SpacingBuilder,
    wrap: Wrap? = Wrap.createWrap(WrapType.NONE, false),
    alignment: Alignment? = Alignment.createAlignment(),
    indent: Indent? = Indent.getNoneIndent(),
    private val leaf: Boolean = node.firstChildNode !is SmithyElement,
    private val children: SmithyBlock.() -> List<SmithyBlock> = { children(this.node, spacingBuilder, this) }
) : AbstractBlock(node, wrap, alignment) {
    private val _indent = indent

    companion object {
        operator fun invoke(node: ASTNode, spacingBuilder: SpacingBuilder, parent: SmithyBlock? = null) = when {
            node.psi is SmithyEntry || node.psi is SmithyMember -> SmithyBlock(
                node,
                spacingBuilder,
                wrap = Wrap.createWrap(WrapType.ALWAYS, true),
                indent = Indent.getNormalIndent()
            )
            //Objects, arrays, and trait arguments (which are a special unwrapped object) will align with the parent, but have children wrapped
            node.psi is SmithyArray || node.psi is SmithyObject || node.psi is SmithyTraitArguments -> SmithyBlock(
                node, spacingBuilder, alignment = parent?.alignment ?: Alignment.createAlignment()
            )
            //Trailing brace should always be wrapped and aligned with the parent block
            node.psi.elementType == SmithyTypes.TOKEN_CLOSE_BRACE || (node.psi.elementType == SmithyTypes.TOKEN_CLOSE_PAREN && node.psi.prevSibling is SmithyTraitValues) -> SmithyBlock(
                node,
                spacingBuilder,
                wrap = Wrap.createWrap(WrapType.ALWAYS, true),
                alignment = parent?.alignment ?: Alignment.createAlignment()
            )
            //Nested line comments should be indented like normal children
            node.elementType == SmithyTypes.TOKEN_LINE_COMMENT && node.psi.parent !is SmithyModel -> SmithyBlock(
                node, spacingBuilder, indent = Indent.getNormalIndent()
            )
            else -> SmithyBlock(node, spacingBuilder)
        }

        private fun children(
            node: ASTNode,
            spacingBuilder: SpacingBuilder,
            parent: SmithyBlock?,
            factory: (ASTNode, SpacingBuilder, SmithyBlock?) -> SmithyBlock = this::invoke
        ) = node.getChildren(TokenSet.forAllMatching { it != TokenType.WHITE_SPACE })
            .map { factory(it, spacingBuilder, parent) }

    }

    override fun isLeaf() = leaf
    override fun getIndent() = _indent
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)
    override fun buildChildren() = children(this)
}