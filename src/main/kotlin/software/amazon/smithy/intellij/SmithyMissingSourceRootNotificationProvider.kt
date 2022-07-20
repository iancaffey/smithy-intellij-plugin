package software.amazon.smithy.intellij

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications

/**
 * An [EditorNotifications.Provider] for a notification alerting the user the current [SmithyFile] is not a part of a source root of the parent module.
 *
 * If a candidate source root is found using [SmithyModule.inferSourceRoot], an action will be added to facilitate adding the source root.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyMissingSourceRootNotificationProvider : EditorNotifications.Provider<EditorNotificationPanel>() {
    companion object {
        val KEY = Key.create<EditorNotificationPanel>("SmithyMissingSourceRootNotification")
    }

    override fun getKey() = KEY
    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project
    ): EditorNotificationPanel? {
        if (file.extension != "smithy") return null
        val module = ModuleUtil.findModuleForFile(file, project) ?: return null
        val model = module.rootManager.modifiableModel
        if (SmithyModule.findSourceRoot(file, model) != null) return null
        val panel = EditorNotificationPanel(fileEditor)
        panel.text = "${file.name} is not within a source root"
        SmithyModule.findContentRoot(file, model)?.let { contentRoot ->
            SmithyModule.inferSourceRoot(file, contentRoot)?.let { sourceRoot ->
                panel.createActionLabel("Add source root", {
                    ApplicationManager.getApplication().runWriteAction {
                        contentRoot.addSourceFolder(sourceRoot, false)
                        model.commit()
                    }
                }, true)
            }
        }
        return panel
    }
}