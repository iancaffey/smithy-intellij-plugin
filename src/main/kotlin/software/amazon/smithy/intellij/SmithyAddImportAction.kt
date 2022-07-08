package software.amazon.smithy.intellij

import com.intellij.codeInsight.hint.QuestionAction
import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep

/**
 * A [QuestionAction] for selecting one of many [SmithyShapeDefinition] options for a [SmithyShapeReference].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAddImportAction(
    val project: Project, val editor: Editor, val file: SmithyFile, val options: List<SmithyShapeDefinition>
) : QuestionAction {
    override fun execute(): Boolean {
        val step = object : BaseListPopupStep<SmithyShapeDefinition>("Imports", options) {
            override fun onChosen(selectedValue: SmithyShapeDefinition?, finalChoice: Boolean): PopupStep<*>? {
                if (finalChoice && selectedValue != null) {
                    doFinalStep {
                        WriteCommandAction.runWriteCommandAction(project) {
                            SmithyElementFactory.addImport(file, selectedValue.shapeId)
                        }
                    }
                }
                return FINAL_CHOICE
            }
        }
        val popup = JBPopupFactory.getInstance().createListPopup(project, step) {
            DefaultPsiElementCellRenderer()
        }
        NavigationUtil.hidePopupIfDumbModeStarts(popup, project)
        popup.showInBestPositionFor(editor)
        return true
    }
}