package software.amazon.smithy.intellij

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyResourceIdentifier
import software.amazon.smithy.intellij.psi.SmithyResourceProperty
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyTraitDefinition

/**
 * An [IconProvider] for [SmithyElement].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyIconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int) = when (element) {
        is SmithyShapeDefinition -> SmithyIcons.SHAPE
        is SmithyMemberDefinition -> SmithyIcons.MEMBER
        is SmithyTraitDefinition -> SmithyIcons.TRAIT
        is SmithyResourceIdentifier -> SmithyIcons.RESOURCE_IDENTIFIER
        is SmithyResourceProperty -> SmithyIcons.RESOURCE_PROPERTY
        else -> null
    }
}