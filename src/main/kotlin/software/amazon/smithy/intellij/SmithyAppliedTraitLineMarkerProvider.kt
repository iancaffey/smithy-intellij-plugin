package software.amazon.smithy.intellij

import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.psi.SmithyAppliedTrait
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
        when (element) {
            is SmithyDefinition -> {
                val appliedTraits = element.appliedTraits.takeIf { it.isNotEmpty() } ?: return
                result.add(
                    NavigationGutterIconBuilder.create(SmithyIcons.Gutter.APPLIED_TRAITS)
                        .setTargets(appliedTraits)
                        .setTooltipText("Externally applied traits")
                        .createLineMarkerInfo((element as? SmithyNamedElement)?.nameIdentifier ?: element)
                )
            }
            is SmithyAppliedTrait -> {
                val target = element.shapeId?.reference?.resolve() ?: element.memberId?.reference?.resolve() ?: return
                result.add(
                    NavigationGutterIconBuilder.create(SmithyIcons.Gutter.APPLIED_TRAIT)
                        .setTarget(target)
                        .setTooltipText("Navigate to target")
                        .createLineMarkerInfo(element.shapeId?.id ?: element.memberId?.member ?: element)
                )
            }
        }
    }
}