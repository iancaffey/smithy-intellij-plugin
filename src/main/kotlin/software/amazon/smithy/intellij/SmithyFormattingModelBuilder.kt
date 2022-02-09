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
    @Suppress("UnstableApiUsage")
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val spacingBuilder = SpacingBuilder(codeStyleSettings, SmithyLanguage)
            //Space after : and ,
            .after(TokenSet.create(SmithyTypes.TOKEN_COLON, SmithyTypes.TOKEN_COMMA)).spaces(1)
            //Spaces around =
            .around(SmithyTypes.TOKEN_EQUALS).spaces(1)
            //Blank lines after all top-level declarations (not before so line comments can be placed)
            .after(
                TokenSet.create(
                    SmithyTypes.APPLIED_TRAIT,
                    SmithyTypes.CONTROL,
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
                    SmithyTypes.UNION
                )
            ).blankLines(1)
            //Line break after all traits
            .after(SmithyAnnotator.TOKENS_REQUIRING_TRAILING_NEW_LINE).lineBreakInCode()
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile, SmithyBlock(formattingContext.node, spacingBuilder), codeStyleSettings
        )
    }
}