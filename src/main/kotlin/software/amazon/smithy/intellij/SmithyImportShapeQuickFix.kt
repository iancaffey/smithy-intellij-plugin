package software.amazon.smithy.intellij

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * An [IntentionAction] to add imports for unresolved [SmithyShapeReference].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyImportShapeQuickFix(val shapeId: String, val file: PsiFile) : BaseIntentionAction() {
    private val options = SmithyShapeResolver.resolve(shapeId, file, exact = false)
    override fun getText() = "Import \"$shapeId\""
    override fun getFamilyName() = "Import"
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) =
        file is SmithyFile && options.isNotEmpty()

    override fun invoke(project: Project, editor: Editor, f: PsiFile?) {
        if (options.isEmpty()) return
        val file = f as? SmithyFile ?: return
        if (options.size == 1) {
            WriteCommandAction.runWriteCommandAction(project) {
                SmithyElementFactory.addImport(file, options.first().shapeId)
            }
        } else {
            SmithyAddImportAction(project, editor, file, options).execute()
        }
    }
}