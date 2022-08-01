package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.siblings
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * An [IntentionAction] to remove a shape id within a mixins list.
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyRemoveMixinQuickFix(val shapeId: SmithyShapeId) : BaseIntentionAction() {
    override fun getText() = "Remove '${shapeId.shapeName}' mixin"
    override fun getFamilyName() = "Remove mixin"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val toRemove = shapeId.siblings().takeWhile {
            it == shapeId || it is PsiWhiteSpace || it.elementType == SmithyTypes.TOKEN_COMMA
        }.toList().reversed()
        WriteCommandAction.runWriteCommandAction(project) {
            toRemove.forEach { if (it.isValid) it.delete() }
        }
    }
}