package software.amazon.smithy.intellij.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import com.intellij.util.io.VoidDataExternalizer
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.psi.SmithyAppliedTrait

/**
 * A [FileBasedIndex] which caches occurrences of [SmithyAppliedTrait] to shapes by the target shape name.
 *
 * @author Ian Caffey
 * @since 1.0
 * @see <a href="https://awslabs.github.io/smithy/1.0/spec/core/model.html#applying-traits-externally">Applying traits externally</a>
 */
class SmithyAppliedTraitShapeIndex : SmithyStringIndex<Void?>(excludeAst = true) {
    companion object {
        val NAME = ID.create<String, Void?>("smithy.applied-traits-by-shape-name")
        fun getFiles(shapeName: String, scope: GlobalSearchScope): Collection<VirtualFile> {
            val project = scope.project ?: return emptyList()
            if (DumbService.isDumb(project)) return emptyList()
            return FileBasedIndex.getInstance().getContainingFiles(NAME, shapeName, scope)
        }
    }

    override fun getName() = NAME
    override fun getVersion() = 2
    override fun getValueExternalizer(): VoidDataExternalizer = VoidDataExternalizer.INSTANCE
    override fun process(file: SmithyFile) = file.model?.appliedTraits?.mapNotNull {
        it.shapeId?.let { id -> id.shapeName to null }
    }?.toMap() ?: emptyMap()
}
