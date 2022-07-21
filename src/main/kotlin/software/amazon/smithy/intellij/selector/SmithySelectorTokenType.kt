package software.amazon.smithy.intellij.selector

import com.intellij.psi.tree.IElementType

/**
 * A token element within the AST of a [Smithy](https://awslabs.github.io/smithy) model file.
 *
 * All notable tokens will have a wrapped [SmithyElementType]
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyLexer
 */
class SmithySelectorTokenType(debugName: String) : IElementType(debugName, SmithySelectorLanguage)
