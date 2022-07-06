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
class SmithyImportShapeQuickFix(val project: Project, val shapeId: String) : BaseIntentionAction() {
    private val options = SmithyShapeResolver.resolve(project, shapeId, exact = false)
    override fun getText() = "Import \"$shapeId\""
    override fun getFamilyName() = "Import"
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) =
        file is SmithyFile && options.isNotEmpty()

    override fun invoke(project: Project, editor: Editor, f: PsiFile?) {
        if (options.isEmpty()) return
        val file = f as? SmithyFile ?: return
        if (options.size == 1) {
            SmithyShapeResolver.shapeIdOf(options.first())?.let {
                WriteCommandAction.runWriteCommandAction(project) {
                    SmithyElementFactory.addImport(file, it)
                }
            }
        } else {
            SmithyAddImportAction(project, editor, file, options).execute()
        }
    }
}