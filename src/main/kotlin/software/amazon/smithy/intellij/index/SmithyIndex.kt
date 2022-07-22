package software.amazon.smithy.intellij.index

import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.FileContent
import com.intellij.util.io.EnumeratorStringDescriptor
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.SmithyJson

/**
 * A [FileBasedIndex] which can cache data from AST or IDL within the project.
 *
 * @author Ian Caffey
 * @since 1.0
 */
abstract class SmithyIndex<K, V>(
    val excludeAst: Boolean = false,
    val excludePsi: Boolean = false
) : FileBasedIndexExtension<K, V>() {
    final override fun dependsOnFileContent() = true
    final override fun traceKeyHashToVirtualFileMapping() = true
    final override fun getInputFilter() = FileBasedIndex.InputFilter {
        (!excludeAst && it.extension == "json") || (!excludePsi && it.extension == "smithy")
    }

    open fun process(ast: SmithyAst): Map<K, V> = emptyMap()
    open fun process(file: SmithyFile): Map<K, V> = emptyMap()
    final override fun getIndexer() = DataIndexer<K, V, FileContent> { inputData ->
        when (inputData.file.extension) {
            "json" -> try {
                //Note: all AST will have a "smithy" version field, so we can quickly ignore irrelevant files without parsing the JSON
                inputData.contentAsText.takeIf { "\"smithy\"" in it }?.let {
                    process(SmithyJson.readValue<SmithyAst>(it.toString()))
                } ?: emptyMap()
            } catch (e: Exception) {
                //Note: there's no way to filter down to only Smithy AST JSON files, so any parsing exception will be
                //treated as an invalid or irrelevant JSON file
                emptyMap()
            }
            "smithy" -> (inputData.psiFile as? SmithyFile)?.let { process(it) } ?: emptyMap()
            else -> emptyMap()
        }
    }
}

abstract class SmithyStringIndex<V>(
    excludeAst: Boolean = false,
    excludePsi: Boolean = false
) : SmithyIndex<String, V>(excludeAst, excludePsi) {
    final override fun getKeyDescriptor() = EnumeratorStringDescriptor()
}
