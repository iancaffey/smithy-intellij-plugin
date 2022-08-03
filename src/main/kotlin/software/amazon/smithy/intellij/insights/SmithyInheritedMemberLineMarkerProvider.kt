package software.amazon.smithy.intellij.insights

import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil.getDeepestFirst
import software.amazon.smithy.intellij.SmithyIcons
import software.amazon.smithy.intellij.psi.SmithyElidedMember
import software.amazon.smithy.intellij.psi.SmithyResourceIdentifierDefinition
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
        when (element) {
            is SmithyShapeDefinition -> {
                val inherited = element.members.mapNotNull {
                    (it as? SmithySyntheticMember)?.original
                }
                if (inherited.isNotEmpty()) {
                    result.add(
                        NavigationGutterIconBuilder.create(SmithyIcons.Gutter.INHERITED_MEMBER)
                            .setTargets(inherited)
                            .setTooltipText("Inherited members")
                            .createLineMarkerInfo(getDeepestFirst(element.nameIdentifier))
                    )
                }
            }
            is SmithyElidedMember -> {
                element.declaration?.let {
                    val (label, icon) = when (it) {
                        is SmithyResourceIdentifierDefinition -> "resource identifier" to SmithyIcons.Gutter.INHERITED_RESOURCE_IDENTIFIER
                        else -> "member" to SmithyIcons.Gutter.INHERITED_MEMBER
                    }
                    result.add(
                        NavigationGutterIconBuilder.create(icon)
                            .setTarget(it)
                            .setTooltipText("Targets $label in '${it.enclosingShape.name}'")
                            .createLineMarkerInfo(getDeepestFirst(element.nameIdentifier))
                    )
                }

            }
        }
    }
}