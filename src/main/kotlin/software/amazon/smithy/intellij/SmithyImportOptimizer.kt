package software.amazon.smithy.intellij

import com.intellij.lang.ImportOptimizer
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyShapeId

/**
 * An [ImportOptimizer] for [SmithyImport].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyImportOptimizer : ImportOptimizer {
    companion object {
        fun unusedImports(file: PsiFile): List<SmithyImport> {
            if (file !is SmithyFile) return emptyList()
            val imports = PsiTreeUtil.getChildrenOfTypeAsList(file.model, SmithyImport::class.java)
            if (imports.isEmpty()) return emptyList()
            val shapeIds = PsiTreeUtil.collectElementsOfType(file, SmithyShapeId::class.java)
            return imports.filter { import ->
                import.shapeId.declaredNamespace == import.shapeId.enclosingNamespace || shapeIds.none { shapeId ->
                    shapeId.shapeName == import.shapeId.shapeName && shapeId.parent !is SmithyImport
                }
            }
        }

        fun removeUnusedImports(file: PsiFile) {
            WriteCommandAction.runWriteCommandAction(file.project) {
                unusedImports(file).forEach { it.delete() }
            }
        }
    }

    override fun supports(file: PsiFile) = file is SmithyFile
    override fun processFile(file: PsiFile) = Runnable {
        if (file !is SmithyFile) return@Runnable
        removeUnusedImports(file)
        val remainingImports = PsiTreeUtil.getChildrenOfTypeAsList(file.model, SmithyImport::class.java)
        if (remainingImports.isEmpty()) return@Runnable
        val sortedImports = remainingImports.toMutableList().sortedBy { it.shapeId.id }
        if (remainingImports == sortedImports) return@Runnable
        remainingImports.forEach { it.delete() }
        sortedImports.forEach { SmithyElementFactory.addImport(file, it.shapeId.id) }
    }
}