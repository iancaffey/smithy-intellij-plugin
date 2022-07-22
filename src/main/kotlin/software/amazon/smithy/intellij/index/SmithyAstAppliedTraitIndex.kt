package software.amazon.smithy.intellij.index

import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyAst.AppliedTrait
import software.amazon.smithy.intellij.SmithyJson
import java.io.DataInput
import java.io.DataOutput

/**
 * A [FileBasedIndex] which caches externally applied traits which target absolute shape ids within [SmithyAst].
 *
 * @author Ian Caffey
 * @since 1.0
 * @see <a href="https://awslabs.github.io/smithy/1.0/spec/core/model.html#applying-traits-externally">Applying traits externally</a>
 */
class SmithyAstAppliedTraitIndex : SmithyStringIndex<AppliedTrait>(excludePsi = true) {
    companion object {
        val NAME = ID.create<String, AppliedTrait>("smithy.ast-applied-traits")
        fun forEach(namespace: String, shapeName: String, scope: GlobalSearchScope, action: (AppliedTrait) -> Unit) {
            forEach("$namespace#$shapeName", scope, action)
        }

        fun forEach(
            namespace: String,
            shapeName: String,
            memberName: String,
            scope: GlobalSearchScope,
            action: (AppliedTrait) -> Unit
        ) {
            forEach("$namespace#$shapeName$$memberName", scope, action)
        }

        private fun forEach(shapeId: String, scope: GlobalSearchScope, action: (AppliedTrait) -> Unit) {
            FileBasedIndex.getInstance().processValues(NAME, shapeId, null, { _, trait ->
                action(trait)
                true
            }, scope)
        }
    }

    override fun getName() = NAME
    override fun getVersion() = 0
    override fun getValueExternalizer() = object : DataExternalizer<AppliedTrait> {
        override fun read(`in`: DataInput) = SmithyJson.readValue<AppliedTrait>(`in`.readUTF())
        override fun save(out: DataOutput, appliedTrait: AppliedTrait) {
            out.writeUTF(SmithyJson.writeValueAsString(appliedTrait))
        }
    }

    override fun process(ast: SmithyAst) = ast.shapes?.entries?.filter { it.value is AppliedTrait }?.associate {
        it.key to it.value as AppliedTrait
    } ?: emptyMap()
}
