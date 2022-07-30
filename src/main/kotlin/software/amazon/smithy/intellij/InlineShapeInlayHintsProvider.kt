package software.amazon.smithy.intellij

import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.psi.SmithyInput
import software.amazon.smithy.intellij.psi.SmithyOutput

/**
 * An [InlayParameterHintsProvider] which displays the shape name of inlined [SmithyInput]/[SmithyOutput].
 *
 * @author Ian Caffey
 * @since 1.0
 */
@Suppress("UnstableApiUsage")
class InlineShapeInlayHintsProvider : InlayParameterHintsProvider {
    override fun getDefaultBlackList() = emptySet<String>()
    override fun getBlackListDependencyLanguage() = SmithyLanguage
    override fun getInlayPresentation(inlayText: String) = inlayText
    override fun getParameterHints(element: PsiElement) = when (element) {
        is SmithyInput -> InlayInfo(element.shapeName, element.body.textOffset)
        is SmithyOutput -> InlayInfo(element.shapeName, element.body.textOffset)
        else -> null
    }?.let { listOf(it) } ?: emptyList()
}