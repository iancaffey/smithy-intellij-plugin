package software.amazon.smithy.intellij

import com.intellij.lang.parameterInfo.CreateParameterInfoContext
import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.lang.parameterInfo.ParameterInfoUIContext
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition

/**
 * A [ParameterInfoHandler] to display quick navigation info for [SmithyKey].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyKeyParameterInfoHandler : ParameterInfoHandler<SmithyKey, SmithyMemberDefinition> {
    private val docs = SmithyDocumentationProvider()
    override fun findElementForParameterInfo(context: CreateParameterInfoContext) =
        findKey(context.file, context.offset)?.also {
            it.reference.resolve()?.let { member ->
                context.itemsToShow = arrayOf(member)
            }
        }

    override fun showParameterInfo(key: SmithyKey, context: CreateParameterInfoContext) {
        context.showHint(key, key.textOffset, this)
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext) =
        findKey(context.file, context.offset)

    override fun updateParameterInfo(key: SmithyKey, context: UpdateParameterInfoContext) {
    }

    override fun updateUI(member: SmithyMemberDefinition, context: ParameterInfoUIContext) {
        docs.getQuickNavigateInfo(member, member)?.let { context.setupRawUIComponentPresentation(it) }
    }

    private fun findKey(file: PsiFile, offset: Int) =
        getParentOfType(file.findElementAt(offset), SmithyKey::class.java)
}