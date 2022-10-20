package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiParserFacade
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import com.intellij.psi.util.elementType
import software.amazon.smithy.intellij.psi.SmithyTraitBody
import software.amazon.smithy.intellij.psi.SmithyTypes
import software.amazon.smithy.intellij.psi.SmithyValue

/**
 * An [IntentionAction] to remove all unnecessary commas (which were made optional in IDL 2.0).
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyRemoveCommasQuickFix : BaseIntentionAction() {
    fun isUnnecessaryComma(element: PsiElement): Boolean {
        //Note: commas are only required in traits or node values which are defined on a single line.
        if (element.elementType != SmithyTypes.TOKEN_COMMA) return false
        val parent = getParentOfType(element, SmithyValue::class.java)
            ?: getParentOfType(element, SmithyTraitBody::class.java)
            ?: element.parent
        val document = element.containingFile.viewProvider.document
        return document.getLineNumber(parent.textOffset) != document.getLineNumber(parent.textOffset + parent.textLength)
    }

    override fun getText() = "Remove unnecessary commas"
    override fun getFamilyName() = "Remove unnecessary commas"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) = true
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val commas = PsiTreeUtil.collectElements(file) { isUnnecessaryComma(it) }
        WriteCommandAction.runWriteCommandAction(project) {
            commas.forEach {
                if (it.nextSibling is PsiWhiteSpace) it.delete() else it.replace(
                    project.getService(PsiParserFacade::class.java).createWhiteSpaceFromText(" ")
                )
            }
        }
    }
}