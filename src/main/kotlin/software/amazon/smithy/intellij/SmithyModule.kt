package software.amazon.smithy.intellij

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

/**
 * A utility class for introspecting and modifying the enclosing [Module] containing [Smithy](https://awslabs.github.io/smithy) models.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyModule {
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

    fun findContentRoot(file: VirtualFile, model: ModifiableRootModel) = model.contentEntries.find {
        val contentRoot = it.file ?: return@find false
        VfsUtil.isAncestor(contentRoot, file, true)
    }

    fun findSourceRoot(file: VirtualFile, model: ModifiableRootModel) = model.sourceRoots.find {
        VfsUtil.isAncestor(it, file, true)
    }
}