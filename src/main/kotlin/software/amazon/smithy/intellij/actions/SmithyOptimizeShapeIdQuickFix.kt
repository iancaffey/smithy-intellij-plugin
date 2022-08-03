package software.amazon.smithy.intellij.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.SmithyElementFactory
import software.amazon.smithy.intellij.SmithyFile
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
    ).any { shapeId.shapeName == it.shapeId.shapeName }
    val requiresImport = !hasImport && shapeId.declaredNamespace != shapeId.enclosingNamespace

    override fun getText() =
        if (requiresImport) "Add import for '${shapeId}'" else "Remove unnecessary qualifier for '${shapeId.shapeName}'"

    override fun getFamilyName() = if (requiresImport) "Add import" else "Remove unnecessary qualifier"
    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) =
        file is SmithyFile && shapeId.declaredNamespace != null

    override fun invoke(project: Project, editor: Editor, f: PsiFile?) {
        val namespace = shapeId.declaredNamespace ?: return
        val file = shapeId.containingFile as? SmithyFile ?: return
        val shapeIds = PsiTreeUtil.collectElementsOfType(file, SmithyShapeId::class.java).filter {
            it.declaredNamespace == namespace && it.shapeName == shapeId.shapeName && it.parent !is SmithyImport
        }
        WriteCommandAction.runWriteCommandAction(project) {
            if (requiresImport) {
                SmithyElementFactory.addImport(file, namespace, shapeId.shapeName)
            }
            shapeIds.forEach { it.replace(SmithyElementFactory.createShapeId(project, null, it.shapeName)) }
        }
    }
}