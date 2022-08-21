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
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import software.amazon.smithy.intellij.SmithyShapeResolver.getDefinitions
import software.amazon.smithy.intellij.psi.SmithyArray
import software.amazon.smithy.intellij.psi.SmithyControl
import software.amazon.smithy.intellij.psi.SmithyDefinition
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyMemberId
import software.amazon.smithy.intellij.psi.SmithyMemberInitializer
import software.amazon.smithy.intellij.psi.SmithyMetadata
import software.amazon.smithy.intellij.psi.SmithyObject
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyShapeTarget
import software.amazon.smithy.intellij.psi.SmithySyntheticElement
import software.amazon.smithy.intellij.psi.SmithySyntheticMember
import software.amazon.smithy.intellij.psi.SmithySyntheticShapeTarget
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
sealed class SmithyReference(element: PsiElement, soft: Boolean) : PsiReferenceBase<PsiElement>(element, soft) {
    private var range = TextRange.from(0, element.textLength)
    override fun getRangeInElement(): TextRange {
        element.textLength.takeIf { it != range.length }?.let { range = TextRange.from(0, it) }
        return range
    }

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
data class SmithyKeyReference(val key: SmithyKey) : SmithyReference(key, soft = false) {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(ref: Ref) = CachedValueProvider {
            val parent = if (ref.enclosing != null) ref.enclosing.reference.resolve() else ref.trait.resolve()
            CachedValueProvider.Result.create(
                parent?.let {
                    when (it.shapeId) {
                        "smithy.api#Example" -> {
                            val target = when (ref.memberName) {
                                "input" -> (ref.trait.target as? SmithyShapeDefinition)?.input
                                "output" -> (ref.trait.target as? SmithyShapeDefinition)?.output
                                else -> null
                            }
                            if (target != null) return@let SmithySyntheticMember(parent, ref.memberName).apply {
                                declaredTarget = target
                            }
                        }
                        "smithy.api#ExampleError" -> {
                            if (ref.memberName == "content") {
                                val target = ref.enclosing?.fields?.get("shapeId") as? SmithyShapeTarget
                                if (target != null) return@let SmithySyntheticMember(parent, ref.memberName).apply {
                                    declaredTarget = target
                                }
                            }
                        }
                    }
                    //Note: nested members within documents are also considered documents
                    if (it.type == "document") SmithySyntheticMember(it, ref.memberName).apply {
                        declaredTarget = SmithySyntheticShapeTarget(this, "smithy.api", "Document", "document")
                    } else it.getMember(if (parent.type == "map") "value" else ref.memberName)
                }, dependencies
            )
        }
    }

    //Note: since the reference depends on the parent PSI context, we need to manually keep track of any project PSI
    //modifications, to refresh the internal reference context (but also cache it since this is called very frequently
    //when displaying annotations and documentation in the editor as code is being browsed)
    private val modificationTracker = key.project.getService(PsiModificationTracker::class.java)
    private var _ref: Ref? = null
    private var lastModCount: Long? = null
    private val ref: Ref?
        get() {
            modificationTracker.modificationCount.takeIf { it != lastModCount }?.let { modCount ->
                _ref = (key.parent as? SmithyEntry)?.let { entry ->
                    val name = entry.name
                    val trait = getParentOfType(entry, SmithyTrait::class.java)
                    val parent = entry.parent as? SmithyObject
                    if (trait != null) Ref(key, trait, parent, name) else null
                }
                lastModCount = modCount
            }
            return _ref
        }

    override fun isSoft() = ref.let {
        it == null || (it.enclosing?.reference ?: it.trait.shape.reference).let { enclosing ->
            enclosing.isSoft || enclosing.resolve()?.type == "document"
        }
    }

    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve() = ref?.let { getCachedValue(it, resolver(it)) }
    override fun handleElementRename(newElementName: String): SmithyKey {
        val textRange = key.textRange
        val document = FileDocumentManager.getInstance().getDocument(key.containingFile.virtualFile)
        document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
        PsiDocumentManager.getInstance(key.project).commitDocument(document)
        return key
    }

    //Note: the path from the enclosing trait to the value can change (e.g. if the enclosing field is renamed), so
    //this PsiElement serves as the representative context for the CachedValue computation (with a well-formed equals/hashCode)
    private data class Ref(
        val key: SmithyKey, val trait: SmithyTrait, val enclosing: SmithyObject?, val memberName: String
    ) : SmithySyntheticElement() {
        override fun getParent() = trait
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
data class SmithyMemberReference(val id: SmithyMemberId) : SmithyReference(id, soft = false) {
    override fun isSoft() = id.shapeId.reference.isSoft
    override fun getAbsoluteRange(): TextRange = id.member.textRange
    override fun resolve() = id.shapeId.reference.resolve()?.getMember(id.memberName)
    override fun handleElementRename(newElementName: String): SmithyMemberId {
        val textRange = id.member.textRange
        val document = FileDocumentManager.getInstance().getDocument(id.containingFile.virtualFile)
        document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
        PsiDocumentManager.getInstance(id.project).commitDocument(document)
        return id
    }
}

/**
 * A [PsiReference] to a [SmithyShapeDefinition].
 *
 * @author Ian Caffey
 * @since 1.0
 */
sealed class SmithyShapeReference(element: PsiElement, soft: Boolean) : SmithyReference(element, soft) {
    companion object {
        operator fun invoke(value: SmithyValue) = if (value is SmithyShapeId) ById(value) else ByValue(value)
    }

    abstract override fun resolve(): SmithyShapeDefinition?
}

private data class ById(val shapeId: SmithyShapeId) : SmithyShapeReference(shapeId, soft = false) {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(shapeId: SmithyShapeId) = CachedValueProvider {
            CachedValueProvider.Result.create(getDefinitions(shapeId).firstOrNull(), dependencies)
        }
    }

    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun handleElementRename(newElementName: String): PsiElement = shapeId.setName(newElementName)
    override fun resolve(): SmithyShapeDefinition? = getCachedValue(shapeId, resolver(shapeId))
}

private data class ByValue(val value: SmithyValue) : SmithyShapeReference(value, soft = false) {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(ref: Ref) = CachedValueProvider {
            CachedValueProvider.Result.create(
                when (val parent = ref.parent) {
                    is SmithyArray -> {
                        val enclosing = parent.reference.resolve()
                        if (enclosing?.type == "document") enclosing else enclosing?.getMember("member")?.resolve()
                    }
                    is SmithyEntry -> parent.key.reference.resolve()?.resolve()
                    is SmithyMemberInitializer -> {
                        val member = parent.enclosingMember
                        val enclosingShape = member.enclosingShape
                        //Note: enum/intEnum members _technically_ target smithy.api#Unit, but for the purposes of shape
                        //references, the enum values will refer to the parent enum shape
                        if (enclosingShape.type.let { it == "enum" || it == "intEnum" }) enclosingShape else member.resolve()
                    }
                    is SmithyTraitBody -> ref.enclosingTrait?.resolve()
                    else -> null //control + key + metadata
                },
                dependencies
            )
        }
    }

    override fun isSoft() = value.parent.let { it is SmithyKey || it is SmithyControl || it is SmithyMetadata }
            || getParentOfType(value, SmithyTrait::class.java) == null

    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve(): SmithyShapeDefinition? =
        Ref(value, value.parent, getParentOfType(value, SmithyTrait::class.java)).let {
            getCachedValue(it, resolver(it))
        }

    override fun handleElementRename(newElementName: String): SmithyValue {
        val textRange = value.textRange
        val document = FileDocumentManager.getInstance().getDocument(value.containingFile.virtualFile)
        document!!.replaceString(textRange.startOffset, textRange.endOffset, newElementName)
        PsiDocumentManager.getInstance(value.project).commitDocument(document)
        return value
    }

    //Note: the path from the enclosing trait to the value can change (e.g. if the enclosing field is renamed), so
    //this PsiElement serves as the representative context for the CachedValue computation (with a well-formed equals/hashCode)
    private data class Ref(
        val value: SmithyValue,
        val enclosingElement: PsiElement,
        val enclosingTrait: SmithyTrait?
    ) : SmithySyntheticElement() {
        override fun getParent() = enclosingElement
    }
}