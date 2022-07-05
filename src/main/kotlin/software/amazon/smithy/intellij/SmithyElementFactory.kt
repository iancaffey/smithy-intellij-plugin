package software.amazon.smithy.intellij

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyElement
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyNamespace

/**
 * A utility class providing methods to create [SmithyElement].
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyElementFactory {
    fun addImport(file: SmithyFile, shapeId: String) {
        val model = file.model!!
        val imports = PsiTreeUtil.getChildrenOfTypeAsList(model, SmithyImport::class.java)
        if (imports.isNotEmpty()) {
            if (imports.any { shapeId == it.shapeId.id }) return
            model.addAfter(createImport(file.project, shapeId), imports.last())
        } else {
            val namespace = PsiTreeUtil.getChildOfType(model, SmithyNamespace::class.java)
            model.addAfter(createImport(file.project, shapeId), namespace)
        }
    }

    fun createImport(project: Project, shapeId: String): SmithyImport {
        val file = createFile(
            project, """
            namespace smithy.tmp
            
            use $shapeId
        """.trimIndent()
        )
        return PsiTreeUtil.getChildOfType(file.model!!, SmithyImport::class.java)!!
    }

    fun createFile(project: Project, content: String) =
        PsiFileFactory.getInstance(project).createFileFromText("tmp.smithy", SmithyFileType, content) as SmithyFile
}