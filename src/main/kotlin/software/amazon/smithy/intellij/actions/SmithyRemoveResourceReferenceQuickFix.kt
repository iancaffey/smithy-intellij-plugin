package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.psi.SmithyResourceReference

/**
 * An [IntentionAction] to remove a resource reference within a structure declaration.
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyRemoveResourceReferenceQuickFix(val reference: SmithyResourceReference) : BaseIntentionAction() {
    override fun getText() = "Remove '${reference.shapeId.shapeName}' resource reference"
    override fun getFamilyName() = "Remove resource reference"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        WriteCommandAction.runWriteCommandAction(project) { reference.delete() }
    }
}