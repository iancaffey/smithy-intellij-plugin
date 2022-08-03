package software.amazon.smithy.intellij.insights

import com.intellij.lang.parameterInfo.CreateParameterInfoContext
import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.lang.parameterInfo.ParameterInfoUIContext
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import software.amazon.smithy.intellij.SmithyDocumentationProvider
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyValue

/**
 * A [ParameterInfoHandler] to display quick navigation info for [SmithyValue].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyValueParameterInfoHandler : ParameterInfoHandler<SmithyValue, SmithyShapeDefinition> {
    private val docs = SmithyDocumentationProvider()
    override fun findElementForParameterInfo(context: CreateParameterInfoContext) =
        findValue(context.file, context.offset)?.also {
            it.reference.resolve()?.let { shape ->
                context.itemsToShow = arrayOf(shape)
            }
        }

    override fun showParameterInfo(value: SmithyValue, context: CreateParameterInfoContext) {
        context.showHint(value, value.textOffset, this)
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext) =
        findValue(context.file, context.offset)

    override fun updateParameterInfo(value: SmithyValue, context: UpdateParameterInfoContext) {
    }

    override fun updateUI(shape: SmithyShapeDefinition, context: ParameterInfoUIContext) {
        docs.getQuickNavigateInfo(shape, shape)?.let { context.setupRawUIComponentPresentation(it) }
    }

    private fun findValue(file: PsiFile, offset: Int) =
        getParentOfType(file.findElementAt(offset), SmithyValue::class.java)
}