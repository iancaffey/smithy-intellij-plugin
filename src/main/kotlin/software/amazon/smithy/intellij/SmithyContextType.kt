package software.amazon.smithy.intellij

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

/**
 * A [TemplateContextType] for [Smithy](https://awslabs.github.io/smithy) providing live templates.
 *
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyContextType : TemplateContextType("Smithy", "Smithy") {
    override fun isInContext(context: TemplateActionContext) = context.file is SmithyFile
}