package software.amazon.smithy.intellij

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import java.nio.charset.StandardCharsets

/**
 * A pseudo-index for the [Smithy](https://awslabs.github.io/smithy) [prelude](https://awslabs.github.io/smithy/1.0/spec/core/prelude-model.html).
 *
 * [SmithyPreludeIndex] serves as a stop-gap until the prelude is modeled as a project SDK for users to control.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyPreludeIndex {
    private val PRELUDE_TEXT =
        this::class.java.getResourceAsStream("/prelude/1.0.smithy")!!.readAllBytes().toString(StandardCharsets.UTF_8)
    private val preludes = mutableMapOf<Project, SmithyFile>()

    fun getPrelude(project: Project) = preludes.getOrPut(project) {
        PsiFileFactory.getInstance(project).createFileFromText(
            "prelude.smithy", SmithyLanguage, PRELUDE_TEXT
        ).apply {
            virtualFile.isWritable = false
            PsiDocumentManager.getInstance(project).getDocument(this)?.setReadOnly(true)
        } as SmithyFile
    }
}