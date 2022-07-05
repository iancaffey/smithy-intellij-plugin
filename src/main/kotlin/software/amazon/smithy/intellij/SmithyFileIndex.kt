package software.amazon.smithy.intellij

import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * A utility class providing query methods for [SmithyFile] on top of the [FileTypeIndex].
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyFileIndex {
    fun forEach(scope: GlobalSearchScope, action: (SmithyFile) -> Unit) {
        if (DumbService.isDumb(scope.project!!)) return
        FileTypeIndex.getFiles(SmithyFileType, scope).forEach {
            (PsiManager.getInstance(scope.project!!).findFile(it) as? SmithyFile)?.let(action)
        }
    }
}