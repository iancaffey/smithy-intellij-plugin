package software.amazon.smithy.intellij

import com.intellij.psi.tree.IElementType

/**
 * An element within the AST of a [Smithy](https://awslabs.github.io/smithy) model file.
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyParser
 */
class SmithyElementType(debugName: String) : IElementType(debugName, SmithyLanguage)
