package software.amazon.smithy.intellij

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.FileViewProvider
import software.amazon.smithy.intellij.psi.SmithyModel

/**
 * A PSI file representing a [Smithy](https://awslabs.github.io/smithy) IDL model file.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SmithyLanguage) {
    val buildConfig get() = ModuleUtil.findModuleForFile(this)?.let { SmithyModule.findBuildConfig(it) }
    val model: SmithyModel? get() = findChildByClass(SmithyModel::class.java)
    override fun getFileType() = SmithyFileType
    override fun toString() = "Smithy File"
}
