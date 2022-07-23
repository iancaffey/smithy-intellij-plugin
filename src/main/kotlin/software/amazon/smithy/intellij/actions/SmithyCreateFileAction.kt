package software.amazon.smithy.intellij.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import software.amazon.smithy.intellij.SmithyIcons

/**
 * A [CreateFileFromTemplateAction] for creating [Smithy](https://awslabs.github.io/smithy) model files.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyCreateFileAction : CreateFileFromTemplateAction(
    "Smithy File", "Create a new Smithy file", SmithyIcons.FILE
) {
    override fun getDefaultTemplateProperty() = "Default.smithy"
    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?) =
        "Creating $newName Smithy file"

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("New Smithy File").addKind("Default", SmithyIcons.FILE, "Default")
    }
}