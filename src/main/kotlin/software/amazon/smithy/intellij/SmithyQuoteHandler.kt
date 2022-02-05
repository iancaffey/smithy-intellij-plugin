package software.amazon.smithy.intellij

import com.intellij.codeInsight.editorActions.QuoteHandler
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.psi.TokenType
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [QuoteHandler] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyQuoteHandler : SimpleTokenSetQuoteHandler(SmithyTypes.TOKEN_STRING, TokenType.BAD_CHARACTER)