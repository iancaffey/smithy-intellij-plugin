package software.amazon.smithy.intellij.index

import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.jetbrains.rd.util.getOrCreate
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.psi.SmithyAstShape
import java.io.DataInput
import java.io.DataOutput

/**
 * A [FileBasedIndex] which caches each individual [SmithyAstShape] (by their absolute shape id) in the project.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAstShapeIndex : FileBasedIndexExtension<String, SmithyAstShape>() {
    companion object {
        val NAME = ID.create<String, SmithyAstShape>("smithy.ast-shapes")
        private val scopes = mutableMapOf<Project, GlobalSearchScope>()

        fun getShapes(namespace: String, shapeName: String, project: Project): List<SmithyAstShape> {
            if (DumbService.isDumb(project)) return emptyList()
            val shapes = mutableListOf<SmithyAstShape>()
            val psi = PsiManager.getInstance(project)
            FileBasedIndex.getInstance().processValues(NAME, "$namespace#$shapeName", null, { file, shape ->
                psi.findFile(file)?.let { shapes += shape.within(it) }
                true
            }, scopes.getOrCreate(project) { GlobalSearchScope.allScope(it) })
            return shapes
        }
    }

    override fun getName() = NAME
    override fun getIndexer() = DataIndexer<String, SmithyAstShape, FileContent> { inputData ->
        val ast = try {
            //Note: all AST will have a "smithy" version field, so we can quickly ignore irrelevant files without parsing the JSON
            inputData.contentAsText.takeIf { "\"smithy\"" in it }?.let {
                SmithyAst.SERIALIZER.readValue<SmithyAst>(it.toString())
            }
        } catch (e: Exception) {
            //Note: there's no way to filter down to only Smithy AST JSON files, so any parsing exception will be
            //treated as an invalid or irrelevant JSON file
            null
        }
        ast?.shapes?.entries?.associate { (shapeId, shape) ->
            shapeId to SmithyAstShape(shapeId, shape)
        } ?: emptyMap()
    }

    override fun getValueExternalizer(): DataExternalizer<SmithyAstShape> = object : DataExternalizer<SmithyAstShape> {
        override fun save(out: DataOutput, value: SmithyAstShape) {
            out.writeUTF(value.shapeId)
            out.writeUTF(SmithyAst.SERIALIZER.writeValueAsString(value.shape))
        }

        override fun read(`in`: DataInput): SmithyAstShape {
            val shapeId = `in`.readUTF()
            val shape = SmithyAst.SERIALIZER.readValue<SmithyAst.Shape>(`in`.readUTF())
            return SmithyAstShape(shapeId, shape)
        }
    }

    override fun getVersion() = 0
    override fun getInputFilter() = FileBasedIndex.InputFilter { it.extension == "json" }
    override fun getKeyDescriptor() = EnumeratorStringDescriptor()
    override fun dependsOnFileContent() = true
    override fun traceKeyHashToVirtualFileMapping() = true
}