package software.amazon.smithy.intellij.insights

import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil.getChildOfType
import software.amazon.smithy.intellij.SmithyLanguage
import software.amazon.smithy.intellij.psi.SmithyContainerBody
import software.amazon.smithy.intellij.psi.SmithyInput
import software.amazon.smithy.intellij.psi.SmithyMixins
import software.amazon.smithy.intellij.psi.SmithyOutput
import software.amazon.smithy.intellij.psi.SmithyResourceReference

/**
 * An [InlayParameterHintsProvider] which displays the shape name of inlined [SmithyInput]/[SmithyOutput].
 *
 * @author Ian Caffey
 * @since 1.0
 */
@Suppress("UnstableApiUsage")
class SmithyInlineShapeInlayHintsProvider : InlayParameterHintsProvider {
    override fun getDefaultBlackList() = emptySet<String>()
    override fun getBlackListDependencyLanguage() = SmithyLanguage
    override fun getInlayPresentation(inlayText: String) = inlayText
    override fun getParameterHints(element: PsiElement) = when (element) {
        is SmithyInput -> InlayInfo(element.shapeName, findAnchor(element).textOffset)
        is SmithyOutput -> InlayInfo(element.shapeName, findAnchor(element).textOffset)
        else -> null
    }?.let { listOf(it) } ?: emptyList()

    private fun findAnchor(element: PsiElement) =
        getChildOfType(element, SmithyResourceReference::class.java)
            ?: getChildOfType(element, SmithyMixins::class.java)
            ?: getChildOfType(element, SmithyContainerBody::class.java)
            ?: element
}