package software.amazon.smithy.intellij

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyArray
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyShapeName
import software.amazon.smithy.intellij.psi.SmithyTrait
import software.amazon.smithy.intellij.psi.SmithyTraitBody

/**
 * A [PsiReference] to a [SmithyShape].
 *
 * @author Ian Caffey
 * @since 1.0
 */
sealed class SmithyShapeReference<T : PsiElement>(
    element: T,
) : PsiReferenceBase<T>(element, TextRange.from(0, element.textLength), false) {
    companion object {
        operator fun invoke(id: SmithyShapeId) = ById(id)
        operator fun invoke(name: SmithyShapeName) = ByName(name)
        operator fun invoke(entry: SmithyEntry) = ByMember(entry)
        operator fun invoke(key: SmithyKey) = ByKey(key)
    }

    abstract val id: String?
    abstract val parent: PsiElement?
    abstract override fun resolve(): SmithyDefinition?
    override fun isSoft() = parent == null

    /**
     * A [PsiReference] from a [SmithyShapeId] to its original [SmithyShape].
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ById(shapeId: SmithyShapeId) : SmithyShapeReference<SmithyShapeId>(shapeId) {
        companion object {
            private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
            private fun resolver(shapeId: SmithyShapeId) = CachedValueProvider {
                val results = SmithyShapeResolver.resolve(shapeId)
                CachedValueProvider.Result.create(if (results.size == 1) results.first() else null, dependencies)
            }
        }

        override val id = shapeId.id
        override val parent: PsiElement? = shapeId.parent
        override fun handleElementRename(newElementName: String) = myElement.setName(newElementName)
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve(): SmithyShapeDefinition? =
            CachedValuesManager.getCachedValue(myElement, resolver(myElement))
    }

    /**
     * A [PsiReference] from a [SmithyShapeName] which can resolve to its original [SmithyShape] using the parent [SmithyShapeId] or [SmithyShape] (for own references).
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ByName(shapeName: SmithyShapeName) : SmithyShapeReference<SmithyShapeName>(shapeName) {
        private val ownRef = shapeName.parent as? SmithyShape
        private val delegate = (shapeName.parent as? SmithyShapeId)?.let { ById(it) }
        override val id = delegate?.id
        override val parent = delegate?.parent
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve() = ownRef ?: delegate?.resolve()
        override fun handleElementRename(newElementName: String): SmithyShapeName {
            val textRange = myElement.textRange
            val document = FileDocumentManager.getInstance().getDocument(myElement.containingFile.virtualFile)
            document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
            PsiDocumentManager.getInstance(myElement.project).commitDocument(document)
            return myElement
        }
    }

    /**
     * A [PsiReference] from a [SmithyEntry] which can resolve to its original [SmithyMember].
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ByMember(entry: SmithyEntry) : SmithyShapeReference<SmithyEntry>(entry) {
        private val delegate = PsiTreeUtil.findFirstParent(entry) { it is SmithyTrait }?.let {
            ById((it as SmithyTrait).shapeId)
        }
        private val path = if (delegate != null) MemberPath.build(entry) else null
        override val id = delegate?.id
        override val parent = delegate?.parent
        override fun handleElementRename(newElementName: String) = myElement.setName(newElementName)
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve(): SmithyMemberDefinition? =
            if (path != null) delegate?.resolve()?.let { path.find(it) } else null

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
     * A [PsiReference] from a [SmithyKey] which can resolve to its original [SmithyMember] using the parent [SmithyEntry].
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ByKey(key: SmithyKey) : SmithyShapeReference<SmithyKey>(key) {
        private val delegate = (myElement.parent as? SmithyEntry)?.let { ByMember(it) }
        override val id = delegate?.id
        override val parent = delegate?.parent
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve() = delegate?.resolve()
        override fun handleElementRename(newElementName: String): SmithyKey {
            val textRange = myElement.textRange
            val document = FileDocumentManager.getInstance().getDocument(myElement.containingFile.virtualFile)
            document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
            PsiDocumentManager.getInstance(myElement.project).commitDocument(document)
            return myElement
        }
    }
}