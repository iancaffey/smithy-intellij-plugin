package software.amazon.smithy.intellij.index

import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyJson
import software.amazon.smithy.intellij.psi.SmithyAstShape
import java.io.DataInput
import java.io.DataOutput

/**
 * A [FileBasedIndex] which caches each individual [SmithyAstShape] (by their absolute shape id) in the project.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAstShapeIndex : SmithyStringIndex<SmithyAstShape>(excludePsi = true) {
    companion object {
        val NAME = ID.create<String, SmithyAstShape>("smithy.ast-shapes")
        fun getShapes(namespace: String, shapeName: String, scope: GlobalSearchScope): List<SmithyAstShape> {
            val project = scope.project ?: return emptyList()
            if (DumbService.isDumb(project)) return emptyList()
            val shapes = mutableListOf<SmithyAstShape>()
            val psi = PsiManager.getInstance(project)
            FileBasedIndex.getInstance().processValues(NAME, "$namespace#$shapeName", null, { file, shape ->
                psi.findFile(file)?.let { shapes += shape.within(it) }
                true
            }, scope)
            return shapes
        }
    }

    override fun getName() = NAME
    override fun getVersion() = 4
    override fun getValueExternalizer(): DataExternalizer<SmithyAstShape> = object : DataExternalizer<SmithyAstShape> {
        override fun save(out: DataOutput, value: SmithyAstShape) {
            out.writeUTF(value.shapeId)
            val json = SmithyJson.writeValueAsString(value.shape)
            out.writeInt(json.length)
            out.writeChars(json)
        }

        override fun read(`in`: DataInput): SmithyAstShape {
            val shapeId = `in`.readUTF()
            val length = `in`.readInt()
            val chars = CharArray(length)
            (0 until length).forEach { i -> chars[i] = `in`.readChar() }
            val shape = SmithyJson.readValue<SmithyAst.Shape>(String(chars))
            return SmithyAstShape(shapeId, shape)
        }
    }

    override fun process(ast: SmithyAst) = ast.definitions?.entries?.mapNotNull { (shapeId, definition) ->
        if (definition is SmithyAst.Shape) shapeId to SmithyAstShape(shapeId, definition) else null
    }?.toMap() ?: emptyMap()
}