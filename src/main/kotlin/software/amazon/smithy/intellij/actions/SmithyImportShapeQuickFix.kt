package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.SmithyElementFactory
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.index.SmithyDefinedShapeIdIndex

/**
 * An [IntentionAction] to add imports for unresolved [SmithyReference].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyImportShapeQuickFix(val shapeName: String, val file: PsiFile) : BaseIntentionAction() {
    private val options = SmithyDefinedShapeIdIndex.getShapeIdsByName(shapeName, file.resolveScope)
    override fun getText() = "Import \"$shapeName\""
    override fun getFamilyName() = "Import"
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) =
        file is SmithyFile && options.isNotEmpty()

    override fun invoke(project: Project, editor: Editor, f: PsiFile?) {
        if (options.isEmpty()) return
        val file = f as? SmithyFile ?: return
        if (options.size == 1) {
            val (namespace, shapeName) = options.first().split('#', limit = 2)
            WriteCommandAction.runWriteCommandAction(project) {
                SmithyElementFactory.addImport(file, namespace, shapeName)
            }
        } else {
            SmithyAddImportAction(project, editor, file, options).execute()
        }
    }
}