package software.amazon.smithy.intellij

import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.psi.SmithyDefinition
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyNamedElement
import software.amazon.smithy.intellij.psi.SmithyShape

/**
 * A [LineMarkerProvider] for externally applied traits to [SmithyShape] and [SmithyMember].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAppliedTraitLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val source = element as? SmithyNamedElement
        val appliedTraits = (source as? SmithyDefinition)?.appliedTraits?.takeIf { it.isNotEmpty() } ?: return
        result.add(
            NavigationGutterIconBuilder.create(AllIcons.Gutter.ExtAnnotation)
                .setTargets(appliedTraits)
                .setTooltipText("Externally applied traits")
                .createLineMarkerInfo(source.nameIdentifier)
        )
    }
}