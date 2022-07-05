package software.amazon.smithy.intellij

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyShapeId

/**
 * An [IntentionAction] to replace qualified [SmithyShapeId] with simple shape names.
 *
 * For any [SmithyShapeId] referring to shapes outside the enclosing namespace, an import will be added.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyOptimizeShapeIdQuickFix(val project: Project, val shapeId: SmithyShapeId) : BaseIntentionAction() {
    val hasImport = PsiTreeUtil.getChildrenOfTypeAsList(
        (shapeId.containingFile as? SmithyFile)?.model, SmithyImport::class.java
    ).any { shapeId.id == it.shapeId.id }
    val requiresImport = shapeId.declaredNamespace != shapeId.enclosingNamespace && !hasImport

    override fun getText() =
        if (requiresImport) "Add import for \"${shapeId}\"" else "Remove unnecessary qualifier for \"${shapeId.name}\""

    override fun getFamilyName() = if (requiresImport) "Add import" else "Remove unnecessary qualifier"
    override fun isAvailable(project: Project, editor: Editor?, file: com.intellij.psi.PsiFile?) =
        file is SmithyFile && shapeId.declaredNamespace != null

    override fun invoke(project: Project, editor: Editor, f: PsiFile?) {
        if (shapeId.declaredNamespace == null) return
        val file = shapeId.containingFile as? SmithyFile ?: return
        WriteCommandAction.runWriteCommandAction(project) {
            if (requiresImport) {
                SmithyElementFactory.addImport(file, shapeId.id)
            }
            shapeId.replace(SmithyElementFactory.createShapeId(project, shapeId.name))
        }
    }
}