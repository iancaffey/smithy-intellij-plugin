package software.amazon.smithy.intellij

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.psi.PsiDirectory
import software.amazon.smithy.intellij.SmithyModule.findDefaultNamespace
import software.amazon.smithy.intellij.SmithyModule.findDefaultVersion
import java.util.*

/**
 * A [DefaultTemplatePropertiesProvider] which provides values from the current [SmithyBuildConfiguration] (if present) for use in creating [Smithy](https://awslabs.github.io/smithy) files.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {
    override fun fillProperties(dir: PsiDirectory, props: Properties) {
        props["SMITHY_VERSION"] = findDefaultVersion(dir) ?: "1.0"
        (findDefaultNamespace(dir) ?: dir.parentDirectory?.let { findDefaultNamespace(it) })?.let {
            props["SMITHY_NAMESPACE"] = it
        }
    }
}