package software.amazon.smithy.intellij.index

import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.indexing.SingleEntryFileBasedIndexExtension
import com.intellij.util.indexing.SingleEntryIndexer
import com.intellij.util.io.DataExternalizer
import software.amazon.smithy.intellij.SmithyBuildConfiguration
import software.amazon.smithy.intellij.SmithyJson
import java.io.DataInput
import java.io.DataOutput

/**
 * A [SingleEntryFileBasedIndexExtension] which caches each [SmithyBuildConfiguration] in the project.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyBuildConfigurationIndex : SingleEntryFileBasedIndexExtension<SmithyBuildConfiguration>() {
    companion object {
        val NAME = ID.create<Int, SmithyBuildConfiguration>("smithy.build-config")
        fun getConfig(element: PsiElement) = getConfig(element.containingFile)
        fun getConfig(file: PsiFile) = file.virtualFile?.let { getConfig(file.project, it) }
        fun getConfig(project: Project, file: VirtualFile): SmithyBuildConfiguration? {
            if (DumbService.isDumb(project)) return null
            val configFile = ProjectFileIndex.getInstance(project).getContentRootForFile(file)
                ?.findFileByRelativePath("smithy-build.json") ?: return null
            return FileBasedIndex.getInstance().getSingleEntryIndexData(NAME, configFile, project)
        }
    }

    override fun getName() = NAME
    override fun getIndexer() = object : SingleEntryIndexer<SmithyBuildConfiguration>(true) {
        override fun computeValue(inputData: FileContent) =
            try {
                SmithyJson.readValue<SmithyBuildConfiguration>(inputData.content)
            } catch (e: Exception) {
                null
            }
    }

    override fun getValueExternalizer(): DataExternalizer<SmithyBuildConfiguration> =
        object : DataExternalizer<SmithyBuildConfiguration> {
            override fun save(out: DataOutput, config: SmithyBuildConfiguration) =
                out.writeUTF(SmithyJson.writeValueAsString(config))

            override fun read(`in`: DataInput) = SmithyJson.readValue<SmithyBuildConfiguration>(`in`.readUTF())
        }

    override fun getVersion() = 0
    override fun getInputFilter() = FileBasedIndex.InputFilter { it.name == "smithy-build.json" }
    override fun dependsOnFileContent() = true
    override fun traceKeyHashToVirtualFileMapping() = true
}