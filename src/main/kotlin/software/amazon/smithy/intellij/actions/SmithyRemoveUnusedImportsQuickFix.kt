package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.SmithyImportOptimizer
import software.amazon.smithy.intellij.psi.SmithyImport

/**
 * An [IntentionAction] to remove all unused [SmithyImport].
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyRemoveUnusedImportsQuickFix : BaseIntentionAction() {
    override fun getText() = "Remove unused imports"
    override fun getFamilyName() = "Remove unused imports"
    override fun isAvailable(project: Project, editor: Editor, file: PsiFile) =
        SmithyImportOptimizer.unusedImports(file).isNotEmpty()

    override fun invoke(project: Project, editor: Editor, file: PsiFile) =
        SmithyImportOptimizer.removeUnusedImports(file)
}