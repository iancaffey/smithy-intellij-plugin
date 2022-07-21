package software.amazon.smithy.intellij

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.PsiDirectory
import java.util.*

/**
 * A [DefaultTemplatePropertiesProvider] which provides values from the current [SmithyBuildConfiguration] (if present) for use in creating [Smithy](https://awslabs.github.io/smithy) files.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {
    override fun fillProperties(dir: PsiDirectory, props: Properties) {
        val config = ModuleUtil.findModuleForFile(dir.virtualFile, dir.project)?.let {
            SmithyModule.findBuildConfig(it)
        }
        val version = config?.version ?: findDefaultVersion(dir) ?: "1.0"
        val defaultNamespace = findDefaultNamespace(dir) ?: dir.parentDirectory?.let { findDefaultNamespace(it) }
        props["SMITHY_VERSION"] = version
        defaultNamespace?.let { props["SMITHY_NAMESPACE"] = it }
    }

    private fun findDefaultNamespace(dir: PsiDirectory) = dir.files.mapNotNull {
        (it as? SmithyFile)?.model?.namespace
    }.minOrNull()

    private fun findDefaultVersion(dir: PsiDirectory) = dir.files.mapNotNull { file ->
        (file as? SmithyFile)?.model?.control?.firstNotNullOfOrNull {
            if (it.name == "version") it.value.asString() else null
        }
    }.minOrNull()
}