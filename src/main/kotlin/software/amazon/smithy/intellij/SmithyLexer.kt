package software.amazon.smithy.intellij

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer

/**
 * A [JFlex](https://jflex.de/)-generated [Lexer] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyLexer : FlexAdapter(_SmithyLexer(null))