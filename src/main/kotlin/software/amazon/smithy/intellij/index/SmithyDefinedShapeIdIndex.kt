package software.amazon.smithy.intellij.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import com.intellij.util.io.VoidDataExternalizer
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyFile

/**
 * A [FileBasedIndex] which caches defined shape ids.
 *
 * [SmithyDefinedShapeIdIndex] provides the necessary API for quickly checking if a specific shape has been defined.
 *
 * @author Ian Caffey
 * @since 1.0
 * @see <a href="https://awslabs.github.io/smithy/1.0/spec/core/idl.html#referring-to-shapes">Referring to shapes</a>
 */
class SmithyDefinedShapeIdIndex : SmithyStringIndex<Void?>() {
    companion object {
        val NAME = ID.create<String, Void?>("smithy.defined-shapes")
        fun exists(namespace: String, shapeName: String, scope: GlobalSearchScope): Boolean {
            val project = scope.project ?: return false
            if (DumbService.isDumb(project)) return false
            var exists = false
            FileBasedIndex.getInstance().processValues(NAME, "$namespace#$shapeName", null, { _, _ ->
                exists = true
                false
            }, scope)
            return exists
        }

        fun forEach(scope: GlobalSearchScope, action: (String) -> Unit) {
            val project = scope.project ?: return
            if (DumbService.isDumb(project)) return
            FileBasedIndex.getInstance().processAllKeys(NAME, {
                action(it)
                true
            }, scope, null)
        }

        fun getFiles(namespace: String, shapeName: String, scope: GlobalSearchScope): Collection<VirtualFile> {
            val project = scope.project ?: return emptyList()
            if (DumbService.isDumb(project)) return emptyList()
            return FileBasedIndex.getInstance().getContainingFiles(NAME, "$namespace#$shapeName", scope)
        }

        fun getShapeIdsByName(shapeName: String, scope: GlobalSearchScope): List<String> {
            val shapeIds = mutableListOf<String>()
            forEach(scope) {
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
    override fun getVersion() = 0
    override fun getValueExternalizer(): VoidDataExternalizer = VoidDataExternalizer.INSTANCE
    override fun process(ast: SmithyAst) = ast.shapes?.entries?.filter {
        it.value !is SmithyAst.AppliedTrait
    }?.associate { it.key to null } ?: emptyMap()

    override fun process(file: SmithyFile) = file.model?.shapes?.associate {
        "${it.namespace}#${it.shapeName}" to null
    } ?: emptyMap()
}
