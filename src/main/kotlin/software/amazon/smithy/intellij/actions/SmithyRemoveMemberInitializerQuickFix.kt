package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.psi.SmithyMemberInitializer

/**
 * An [IntentionAction] to remove a member initializer (which assigns a default/enum value).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyRemoveMemberInitializerQuickFix(val initializer: SmithyMemberInitializer) : BaseIntentionAction() {
    override fun getText() = "Remove member '${initializer.enclosingMember.name}' initializer"
    override fun getFamilyName() = "Remove member initializer"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        WriteCommandAction.runWriteCommandAction(project) { initializer.delete() }
    }
}