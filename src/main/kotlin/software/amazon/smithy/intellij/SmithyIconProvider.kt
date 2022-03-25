package software.amazon.smithy.intellij

import com.intellij.icons.AllIcons
import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.psi.SmithyElement
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyShape

/**
 * An [IconProvider] for [SmithyElement].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyIconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int) = when (element) {
        is SmithyShape -> AllIcons.Nodes.Static //Used since it displays as [s]
        is SmithyMember -> AllIcons.Nodes.Method //Used since it displays as [m]
        else -> null
    }
}