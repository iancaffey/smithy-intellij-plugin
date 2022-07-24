package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiParserFacade
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * An [IntentionAction] to remove all unnecessary commas (which were made optional in IDL 2.0).
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyRemoveCommasQuickFix : BaseIntentionAction() {
    override fun getText() = "Remove unnecessary commas"
    override fun getFamilyName() = "Remove unnecessary commas"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val commas = PsiTreeUtil.collectElements(file) { it.elementType == SmithyTypes.TOKEN_COMMA }
        commas.forEach {
            if (it.nextSibling is PsiWhiteSpace) it.delete() else it.replace(
                project.getService(PsiParserFacade::class.java).createWhiteSpaceFromText(" ")
            )
        }
    }
}