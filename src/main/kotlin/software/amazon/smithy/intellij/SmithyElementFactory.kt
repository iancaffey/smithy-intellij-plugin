package software.amazon.smithy.intellij

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiTreeUtil.getChildOfType
import software.amazon.smithy.intellij.SmithyModule.defaultNamespace
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
    fun addImport(file: SmithyFile, namespace: String, shapeName: String) {
        val model = file.model!!
        val imports = PsiTreeUtil.getChildrenOfTypeAsList(model, SmithyImport::class.java)
        if (imports.isNotEmpty()) {
            if (imports.any { shapeName == it.shapeId.shapeName && namespace == it.shapeId.declaredNamespace }) return
            val newImport = createImport(file.project, namespace, shapeName)
            val sortedImports = imports.toMutableList().plus(newImport).sortedWith(
                compareBy<SmithyImport> { it.shapeId.declaredNamespace }.thenBy { it.shapeId.shapeName }
            )
            val insertIndex = sortedImports.indexOf(newImport)
            if (insertIndex == 0) {
                model.addBefore(newImport, sortedImports[1])
            } else {
                model.addAfter(newImport, sortedImports[insertIndex - 1])
            }
        } else {
            model.addAfter(
                createImport(file.project, namespace, shapeName),
                getChildOfType(model, SmithyNamespace::class.java) ?: model.add(
                    createNamespace(file.project, defaultNamespace(file))
                )
            )
        }
    }

    fun createImport(project: Project, namespace: String?, shapeName: String): SmithyImport {
        val file = createFile(
            project, """
            namespace smithy.tmp
            
            use ${if (namespace != null) "$namespace#$shapeName" else shapeName}
        """.trimIndent()
        )
        return file.model!!.imports.first()
    }

    fun createNamespace(project: Project, namespace: String): SmithyNamespace {
        val file = createFile(project, "namespace $namespace")
        return getChildOfType(file.model!!, SmithyNamespace::class.java)!!
    }

    fun createShapeId(project: Project, namespace: String?, shapeName: String) =
        createImport(project, namespace, shapeName).shapeId

    fun createFile(project: Project, content: String) =
        PsiFileFactory.getInstance(project).createFileFromText("tmp.smithy", SmithyFileType, content) as SmithyFile
}