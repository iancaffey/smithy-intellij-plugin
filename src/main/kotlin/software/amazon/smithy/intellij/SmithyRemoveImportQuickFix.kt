package software.amazon.smithy.intellij

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.psi.SmithyImport

/**
 * An [IntentionAction] to remove a [SmithyImport].
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyRemoveImportQuickFix(val import: SmithyImport) : BaseIntentionAction() {
    override fun getText() = "Remove import '${import.shapeId.id}'"
    override fun getFamilyName() = "Remove import"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) = import.delete()
}