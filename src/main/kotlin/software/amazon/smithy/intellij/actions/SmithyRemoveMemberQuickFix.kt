package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.siblings
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * An [IntentionAction] to remove a [SmithyMemberDefinition] and all trailing whitespace.
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyRemoveMemberQuickFix(val member: SmithyMemberDefinition) : BaseIntentionAction() {
    override fun getText() = "Remove member '${member.name}'"
    override fun getFamilyName() = "Remove member"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) = member.siblings().takeWhile {
        it == member || it is PsiWhiteSpace || it.elementType == SmithyTypes.TOKEN_COMMA
    }.toList().forEach { if (it.isValid) it.delete() }
}