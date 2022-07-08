package software.amazon.smithy.intellij

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager.getCachedValue
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyArray
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMemberId
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyTrait
import software.amazon.smithy.intellij.psi.SmithyTraitBody
import software.amazon.smithy.intellij.psi.SmithyValue

/**
 * A [PsiReference] to a [SmithyDefinition].
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyKeyReference
 * @see SmithyMemberReference
 * @see SmithyShapeReference
 */
sealed class SmithyReference<T : PsiElement>(
    element: T, soft: Boolean
) : PsiReferenceBase<T>(element, TextRange.from(0, element.textLength), soft) {
    abstract override fun resolve(): SmithyDefinition?
}

/**
 * A [PsiReference] from a [SmithyKey] to a [SmithyMemberDefinition].
 *
 * [SmithyKey] from fields within a [SmithyTrait] are resolved to their corresponding nested member.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyKeyReference(key: SmithyKey) : SmithyReference<SmithyKey>(key, key.parent !is SmithyEntry) {
    private val entry = key.parent as? SmithyEntry
    private val delegate = PsiTreeUtil.findFirstParent(entry) { it is SmithyTrait }?.let {
        SmithyShapeReference((it as SmithyTrait).shapeId)
    }
    private val path = if (entry != null && delegate != null) MemberPath.build(entry) else null
    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve() = if (path != null) delegate?.resolve()?.let { path.find(it) } else null
    override fun handleElementRename(newElementName: String): SmithyKey {
        val textRange = myElement.textRange
        val document = FileDocumentManager.getInstance().getDocument(myElement.containingFile.virtualFile)
        document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
        PsiDocumentManager.getInstance(myElement.project).commitDocument(document)
        return myElement
    }

    interface MemberPath {
        companion object {
            fun build(entry: SmithyEntry): MemberPath {
                var path = MemberPath(entry.name)
                var current = entry.parent
                while (current != null && current !is SmithyTraitBody) {
                    when (current) {
                        is SmithyArray -> path = MemberPath("member").andThen(path)
                        is SmithyEntry -> path = MemberPath(current.name).andThen(path)
                    }
                    current = current.parent
                }
                return path
            }

            operator fun invoke(name: String) = object : MemberPath {
                override fun find(shape: SmithyShapeDefinition) = shape.getMember(name)
                override fun toString() = name
            }
        }

        fun find(shape: SmithyShapeDefinition): SmithyMemberDefinition?
        fun andThen(next: MemberPath) = object : MemberPath {
            override fun find(shape: SmithyShapeDefinition) = this@MemberPath.find(shape)?.let {
                val results = SmithyShapeResolver.resolve(it)
                if (results.size == 1) next.find(results.first()) else null
            }

            override fun toString() = "${this@MemberPath}.$next"
        }
    }
}

/**
 * A [PsiReference] from a [SmithyMemberId] to a [SmithyMemberDefinition].
 *
 * [SmithyKey] from fields within a [SmithyTrait] are resolved to their corresponding nested member.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyMemberReference(id: SmithyMemberId) : SmithyReference<SmithyMemberId>(id, false) {
    private val delegate = SmithyShapeReference(id.shapeId)
    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve() = delegate.resolve()?.getMember(myElement.memberName.text)
    override fun handleElementRename(newElementName: String): SmithyMemberId {
        val textRange = myElement.textRange
        val document = FileDocumentManager.getInstance().getDocument(myElement.containingFile.virtualFile)
        document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
        PsiDocumentManager.getInstance(myElement.project).commitDocument(document)
        return myElement
    }
}

/**
 * A [PsiReference] to a [SmithyShapeDefinition].
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyShapeReference(val value: SmithyValue) : SmithyReference<SmithyValue>(value, false) {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(shapeId: SmithyShapeId, path: ValuePath?) = CachedValueProvider {
            CachedValueProvider.Result.create((path ?: ValuePath.EMPTY).resolve(shapeId), dependencies)
        }
    }

    private val shapeId =
        (value as? SmithyShapeId) ?: PsiTreeUtil.getParentOfType(value, SmithyTrait::class.java)?.shapeId
    private val path = if (value is SmithyShapeId) null else ValuePath.buildTo(value)
    override fun isSoft() = shapeId == null && path == null
    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve() = shapeId?.let { getCachedValue(value, resolver(shapeId, path)) }
    override fun handleElementRename(newElementName: String): SmithyValue {
        val textRange = myElement.textRange
        val document = FileDocumentManager.getInstance().getDocument(myElement.containingFile.virtualFile)
        document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
        PsiDocumentManager.getInstance(myElement.project).commitDocument(document)
        return myElement
    }
}

/**
 * A path to a value within a [SmithyShapeDefinition].
 *
 * Given a [SmithyShapeId], the [SmithyShapeDefinition] of the target value can be resolved using [resolve].
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class ValuePath(val path: List<String> = emptyList()) {
    companion object {
        val EMPTY = ValuePath()
        fun buildTo(value: SmithyValue): ValuePath? {
            val root = PsiTreeUtil.findFirstParent(value) { it is SmithyTraitBody } ?: return null
            val path = mutableListOf<String>()
            var current: PsiElement = value.parent
            while (current != root) {
                path += if (current is SmithyEntry) current.name else "member"
                current = current.parent
            }
            path.reverse()
            return if (path.isEmpty()) EMPTY else ValuePath(path)
        }
    }

    fun resolve(shapeId: SmithyShapeId): SmithyShapeDefinition? {
        val results = SmithyShapeResolver.resolve(shapeId)
        val root = results.takeIf { it.size == 1 }?.first() ?: return null
        if (path.isEmpty()) return root
        var current: SmithyMemberDefinition? = null
        path.forEach { name ->
            current = current?.let { it.resolve()?.getMember(name) } ?: root.getMember(name) ?: return null
        }
        return current?.resolve()
    }
}