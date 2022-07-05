package software.amazon.smithy.intellij

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndex.ValueProcessor
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.indexing.SingleEntryFileBasedIndexExtension
import com.intellij.util.indexing.SingleEntryIndexer
import com.intellij.util.io.DataExternalizer
import java.io.DataInput
import java.io.DataOutput

/**
 * A [FileBasedIndex] which caches each individual [SmithyAst] in the project.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAstIndex : SingleEntryFileBasedIndexExtension<SmithyAst>() {
    companion object {
        val NAME = ID.create<Int, SmithyAst>("smithy.ast")

        fun forEach(scope: GlobalSearchScope, action: (SmithyAst, VirtualFile) -> Unit) {
            if (DumbService.isDumb(scope.project!!)) return
            val index = FileBasedIndex.getInstance()
            index.processAllKeys(NAME, { key ->
                index.getContainingFiles(NAME, key, scope).forEach {
                    index.processValues(NAME, key, it, ValueProcessor { file, value ->
                        action(value, file)
                        true
                    }, scope)
                }
                true
            }, scope, null)
        }
    }

    override fun getName() = NAME
    override fun getIndexer() = object : SingleEntryIndexer<SmithyAst>(false) {
        override fun computeValue(inputData: FileContent) = try {
            inputData.content.inputStream().use { SmithyAst.parse(it) }
        } catch (e: Exception) {
            //Note: there's no way to filter down to only Smithy AST JSON files, so any parsing exception will be
            //treated as an invalid or irrelevant JSON file
            null
        }
    }

    override fun getValueExternalizer(): DataExternalizer<SmithyAst> = object : DataExternalizer<SmithyAst> {
        override fun save(out: DataOutput, value: SmithyAst) = out.writeUTF(value.toJson())
        override fun read(`in`: DataInput) = SmithyAst.parse(`in`.readUTF())
    }

    override fun getVersion() = 0
    override fun getInputFilter() = FileBasedIndex.InputFilter { it.extension == "json" }
}