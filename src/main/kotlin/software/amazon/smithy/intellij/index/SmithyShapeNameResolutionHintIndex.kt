package software.amazon.smithy.intellij.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil.processElements
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyShapeId
import java.io.DataInput
import java.io.DataOutput

/**
 * A [FileBasedIndex] which caches shape resolution hints for shape names (relative shape ids).
 *
 * Within each file, while we cannot always infer the absolute shape id, we can at least get most of the way there.
 *
 * - If the file has a [SmithyImport] with the same shape name, the [ShapeNameResolutionHint] will resolve to the imported shape id.
 * - If the shape is defined within the file or uses an absolute shape id, the [ShapeNameResolutionHint] will provide the resolved shape id.
 * - Otherwise, the [ShapeNameResolutionHint] will provide the relative shape name, the enclosing namespace, and `null` resolved shape id.
 *
 * An unresolved [ShapeNameResolutionHint] can be used to search for shapes in [ShapeNameResolutionHint.enclosingNamespace] or `smithy.api` to complete the shape resolution.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyShapeNameResolutionHintIndex : FileBasedIndexExtension<String, ShapeNameResolutionHint>() {
    companion object {
        val NAME = ID.create<String, ShapeNameResolutionHint>("smithy.shape-hints")
        fun getHint(shapeName: String, file: PsiFile) = file.virtualFile?.let { getHint(shapeName, file.project, it) }
        fun getHint(shapeName: String, project: Project, file: VirtualFile): ShapeNameResolutionHint? {
            if (DumbService.isDumb(project)) return null
            var hint: ShapeNameResolutionHint? = null
            FileBasedIndex.getInstance().processValues(NAME, shapeName, file, { _, value ->
                hint = value
                false
            }, GlobalSearchScope.fileScope(project, file))
            return hint
        }
    }

    override fun getName() = NAME
    override fun getIndexer() = object : DataIndexer<String, ShapeNameResolutionHint, FileContent> {
        override fun map(inputData: FileContent): Map<String, ShapeNameResolutionHint> {
            val file = inputData.psiFile as? SmithyFile ?: return emptyMap()
            val model = file.model ?: return emptyMap()
            val enclosingNamespace = model.namespace ?: return emptyMap()
            val imports = model.imports
            val shapes = model.shapes
            val hints = mutableMapOf<String, ShapeNameResolutionHint>()
            imports.forEach {
                val shapeName = it.shapeId.shapeName
                if (shapeName in hints) return@forEach
                val namespace = it.shapeId.declaredNamespace ?: return@forEach
                hints[shapeName] = ShapeNameResolutionHint(shapeName, namespace, namespace)
            }
            shapes.forEach {
                val namespace = it.namespace
                val shapeName = it.shapeName
                if (shapeName in hints) return@forEach
                hints[shapeName] = ShapeNameResolutionHint(shapeName, namespace, namespace)
            }
            //All remaining relative shape ids are ambiguous, since there is not an import or shape definition within the file
            processElements(file, SmithyShapeId::class.java) { shapeId ->
                val shapeName = shapeId.shapeName
                if (shapeName !in hints && shapeId.declaredNamespace == null) {
                    hints[shapeName] = ShapeNameResolutionHint(shapeName, enclosingNamespace, null)
                }
                true
            }
            return hints
        }
    }

    override fun getKeyDescriptor() = EnumeratorStringDescriptor()
    override fun getValueExternalizer(): DataExternalizer<ShapeNameResolutionHint> =
        object : DataExternalizer<ShapeNameResolutionHint> {
            override fun save(out: DataOutput, hint: ShapeNameResolutionHint) {
                out.writeUTF(hint.shapeName)
                out.writeUTF(hint.enclosingNamespace)
                out.writeUTF(hint.resolvedNamespace ?: "")
            }

            override fun read(`in`: DataInput) = ShapeNameResolutionHint(
                `in`.readUTF(),
                `in`.readUTF(),
                `in`.readUTF().takeIf { it.isNotEmpty() }
            )
        }

    override fun getVersion() = 0
    override fun getInputFilter() = FileBasedIndex.InputFilter { it.extension == "smithy" }
    override fun dependsOnFileContent() = true
    override fun traceKeyHashToVirtualFileMapping() = true
}

data class ShapeNameResolutionHint(
    val shapeName: String,
    val enclosingNamespace: String,
    val resolvedNamespace: String?
)