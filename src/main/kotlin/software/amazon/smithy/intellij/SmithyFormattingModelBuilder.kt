package software.amazon.smithy.intellij

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.tree.TokenSet
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [FormattingModelBuilder] for [Smithy](https://awslabs.github.io/smithy) model files.
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyBlock
 */
class SmithyFormattingModelBuilder : FormattingModelBuilder {
    companion object {
        val TOKENS_REQUIRING_TRAILING_NEW_LINE = TokenSet.create(
            SmithyTypes.APPLIED_TRAIT,
            SmithyTypes.CONTROL,
            SmithyTypes.DOCUMENTATION,
            SmithyTypes.IMPORT,
            SmithyTypes.LIST,
            SmithyTypes.MAP,
            SmithyTypes.METADATA,
            SmithyTypes.NAMESPACE,
            SmithyTypes.OPERATION,
            SmithyTypes.RESOURCE,
            SmithyTypes.SERVICE,
            SmithyTypes.SET,
            SmithyTypes.SIMPLE_SHAPE,
            SmithyTypes.STRUCTURE,
            SmithyTypes.TOKEN_LINE_COMMENT,
            SmithyTypes.UNION
        )
    }

    @Suppress("UnstableApiUsage")
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val spacingBuilder = SpacingBuilder(codeStyleSettings, SmithyLanguage)
            //No extra spaces around $
            .around(SmithyTypes.TOKEN_DOLLAR_SIGN).none()
            //No extra spaces before :
            .before(TokenSet.create(SmithyTypes.TOKEN_COLON)).none()
            //Space before container bodies (to add separation from the shape name or traits for inline I/O)
            .before(
                TokenSet.create(
                    SmithyTypes.CONTAINER_BODY, SmithyTypes.ENUM_BODY, SmithyTypes.INT_ENUM_BODY
                )
            ).spaces(1)
            //Space after : and ,
            .after(TokenSet.create(SmithyTypes.TOKEN_COLON, SmithyTypes.TOKEN_COMMA)).spaces(1)
            //Spaces around =, :=, for, mixins list, and with
            .around(
                TokenSet.create(
                    SmithyTypes.TOKEN_EQUALS,
                    SmithyTypes.TOKEN_FOR,
                    SmithyTypes.MIXINS,
                    SmithyTypes.TOKEN_WALRUS,
                    SmithyTypes.TOKEN_WITH
                )
            ).spaces(1)
            //Blank lines after all top-level declarations (not before so line comments can be placed)
            .after(
                TokenSet.create(
                    SmithyTypes.LIST,
                    SmithyTypes.MAP,
                    SmithyTypes.NAMESPACE,
                    SmithyTypes.OPERATION,
                    SmithyTypes.RESOURCE,
                    SmithyTypes.SERVICE,
                    SmithyTypes.SET,
                    SmithyTypes.SIMPLE_SHAPE,
                    SmithyTypes.STRUCTURE,
                    SmithyTypes.UNION
                )
            ).blankLines(1)
            //Line break after all traits
            .after(TOKENS_REQUIRING_TRAILING_NEW_LINE).lineBreakInCode()
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile, SmithyBlock(formattingContext.node, spacingBuilder), codeStyleSettings
        )
    }
}