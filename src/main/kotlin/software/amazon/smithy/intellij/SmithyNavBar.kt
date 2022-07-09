package software.amazon.smithy.intellij

import com.intellij.ide.navigationToolbar.NavBarModelExtension
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import software.amazon.smithy.intellij.ext.SmithyElement
import software.amazon.smithy.intellij.ext.SmithyNamedElement

/**
 * A [NavBarModelExtension] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyNavBar : StructureAwareNavBarModelExtension() {
    override val language = SmithyLanguage
    override fun getPresentableText(value: Any?) = (value as? SmithyNamedElement)?.name
    override fun getIcon(value: Any?) = (value as? SmithyElement)?.getIcon(0)
}