package software.amazon.smithy.intellij.insights

import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil.getDeepestFirst
import software.amazon.smithy.intellij.SmithyIcons
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithySyntheticMember

/**
 * A [LineMarkerProvider] for inherited members of [SmithyShapeDefinition].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyInheritedMemberLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val shape = element as? SmithyShapeDefinition ?: return
        val inherited = shape.members.mapNotNull {
            (it as? SmithySyntheticMember)?.original
        }.takeIf { it.isNotEmpty() } ?: return
        result.add(
            NavigationGutterIconBuilder.create(SmithyIcons.Gutter.INHERITED_MEMBER)
                .setTargets(inherited)
                .setTooltipText("Inherited members")
                .createLineMarkerInfo(getDeepestFirst(shape.nameIdentifier))
        )
    }
}