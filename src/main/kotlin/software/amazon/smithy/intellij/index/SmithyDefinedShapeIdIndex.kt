package software.amazon.smithy.intellij.index

import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.indexing.ScalarIndexExtension
import com.intellij.util.io.EnumeratorStringDescriptor
import com.jetbrains.rd.util.getOrCreate
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.SmithyJson

/**
 * A [FileBasedIndex] which caches defined shape ids.
 *
 * [SmithyDefinedShapeIdIndex] provides the necessary API for quickly checking if a specific shape has been defined.
 *
 * @author Ian Caffey
 * @since 1.0
 * @see <a href="https://awslabs.github.io/smithy/1.0/spec/core/idl.html#referring-to-shapes">Referring to shapes</a>
 */
class SmithyDefinedShapeIdIndex : ScalarIndexExtension<String>() {
    companion object {
        val NAME = ID.create<String, Void>("smithy.defined-shapes")
        private val scopes = mutableMapOf<Project, GlobalSearchScope>()

        fun exists(namespace: String, shapeName: String, project: Project): Boolean {
            if (DumbService.isDumb(project)) return false
            val scope = scopes.getOrCreate(project) { GlobalSearchScope.allScope(it) }
            var exists = false
            FileBasedIndex.getInstance().processValues(NAME, "$namespace#$shapeName", null, { _, _ ->
                exists = true
                false
            }, scope)
            return exists
        }

        fun forEach(project: Project, action: (String) -> Unit) {
            if (DumbService.isDumb(project)) return
            FileBasedIndex.getInstance().processAllKeys(NAME, {
                action(it)
                true
            }, project)
        }

        fun getFiles(namespace: String, shapeName: String, project: Project): Collection<VirtualFile> =
            if (DumbService.isDumb(project)) emptyList()
            else FileBasedIndex.getInstance().getContainingFiles(
                NAME, "$namespace#$shapeName", scopes.getOrCreate(project) { GlobalSearchScope.allScope(it) }
            )

        fun getShapeIdsByName(shapeName: String, project: Project): List<String> {
            val shapeIds = mutableListOf<String>()
            forEach(project) {
                //Note: to avoid doing a string split for every defined shape id, we'll do an efficient check for
                //the shape id ending with '#{shapeName}'
                if (it.length > shapeName.length && it[it.length - shapeName.length - 1] == '#' && it.endsWith(shapeName)) {
                    shapeIds += it
                }
            }
            return shapeIds
        }
    }

    override fun getName() = NAME
    override fun getIndexer() = DataIndexer<String, Void, FileContent> { inputData ->
        when (inputData.file.extension) {
            "json" -> try {
                //Note: all AST will have a "smithy" version field, so we can quickly ignore irrelevant files without parsing the JSON
                val ast = inputData.contentAsText.takeIf {
                    "\"smithy\"" in it
                }?.let { SmithyJson.readValue<SmithyAst>(it.toString()) }
                ast?.shapes?.entries?.filter {
                    it.value !is SmithyAst.AppliedTrait
                }?.associate { it.key to null } ?: emptyMap()
            } catch (e: Exception) {
                //Note: there's no way to filter down to only Smithy AST JSON files, so any parsing exception will be
                //treated as an invalid or irrelevant JSON file
                emptyMap()
            }
            "smithy" -> (inputData.psiFile as? SmithyFile)?.model?.shapes?.associate {
                "${it.namespace}#${it.shapeName}" to null
            } ?: emptyMap()
            else -> emptyMap()
        }
    }

    override fun getKeyDescriptor() = EnumeratorStringDescriptor()
    override fun getVersion() = 0
    override fun getInputFilter() = FileBasedIndex.InputFilter { it.extension == "json" || it.extension == "smithy" }
    override fun dependsOnFileContent() = true
    override fun traceKeyHashToVirtualFileMapping() = true
}
