package software.amazon.smithy.intellij.selector

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer

/**
 * A [JFlex](https://jflex.de/)-generated [Lexer] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithySelectorLexer : FlexAdapter(_SmithySelectorLexer(null))