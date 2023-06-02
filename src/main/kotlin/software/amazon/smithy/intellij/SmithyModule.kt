package software.amazon.smithy.intellij

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.index.SmithyBuildConfigurationIndex

/**
 * A utility class for introspecting and modifying the enclosing [Module] containing [Smithy](https://awslabs.github.io/smithy) models.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyModule {
    fun findGradleBuildFile(module: Module) = module.rootManager.contentRoots.firstNotNullOfOrNull {
        //Looks for standard build files first, but still supports non-standard if necessary
        it.children.first { child -> child.name == "build.gradle" || child.name == "build.gradle.kts" }
            ?: it.children.first { child ->
                !child.isDirectory && child.name != ".gradle" && !child.name.startsWith("settings")
                        && (child.name.endsWith(".gradle") || child.name.endsWith(".gradle.kts"))
            }
    }

    fun inferSourceRoot(file: VirtualFile, contentRoot: ContentEntry): VirtualFile? {
        val root = contentRoot.file ?: return null
        val path = root.toNioPath()
        val relative = path.relativize(file.toNioPath())
        return when {
            relative.startsWith("model") -> path.resolve("model")
            relative.startsWith("smithy") -> path.resolve("smithy")
            else -> null
        }?.let { VirtualFileManager.getInstance().findFileByNioPath(it) }
    }

    fun findBuildConfig(module: Module) = findBuildFile(module)?.let {
        SmithyBuildConfigurationIndex.getConfig(it, module.project)
    }

    fun findBuildFile(module: Module): VirtualFile? {
        val roots = module.rootManager.contentRoots
        roots.firstNotNullOfOrNull { it.findFileByRelativePath("smithy-build.json") }?.let { return it }
        //Maven-style ("main" module content root needs to be traversed 2 directories up to /src/main to find the build config)
        return roots.firstNotNullOfOrNull { root ->
            root.takeIf { it.name == "main" }
                ?.parent?.takeIf { it.name == "src" }
                ?.parent?.findFileByRelativePath("smithy-build.json")
        }
    }

    fun findContentRoot(file: VirtualFile, model: ModifiableRootModel) = model.contentEntries.find {
        val contentRoot = it.file ?: return@find false
        VfsUtil.isAncestor(contentRoot, file, true)
    }

    fun findSourceRoot(file: VirtualFile, model: ModifiableRootModel) = model.sourceRoots.find {
        VfsUtil.isAncestor(it, file, true)
    }

    fun defaultNamespace(file: PsiFile) =
        file.parent?.let { findDefaultNamespace(it) }
            ?: file.parent?.parent?.let { findDefaultNamespace(it) }
            ?: file.name.replace(Regex("[^A-Za-z0-9_.]"), "").split(".").filter { it.isEmpty() }.joinToString(".")
                .let { name ->
                    when {
                        name.isEmpty() -> "smithy"
                        name[0] in 'A'..'Z' || name[0] == '_' -> name
                        else -> "_$name"
                    }
                }

    fun findDefaultNamespace(dir: PsiDirectory) = dir.files.mapNotNull {
        (it as? SmithyFile)?.model?.namespace
    }.minOrNull()

    fun findDefaultVersion(dir: PsiDirectory) = dir.files.mapNotNull { file ->
        (file as? SmithyFile)?.model?.version
    }.minOrNull()
}