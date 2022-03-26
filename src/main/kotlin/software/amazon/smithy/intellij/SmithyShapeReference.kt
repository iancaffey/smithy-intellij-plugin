package software.amazon.smithy.intellij

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.rd.util.getOrCreate
import software.amazon.smithy.intellij.psi.SmithyAggregateShape
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyShapeName
import software.amazon.smithy.intellij.psi.SmithyTrait
import java.nio.charset.StandardCharsets

/**
 * A [PsiReference] from a [SmithyShapeId] to its original [SmithyShape].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyShapeReference(shapeId: SmithyShapeId) : PsiReferenceBase<SmithyShapeId>(
    shapeId, TextRange.from(0, shapeId.textLength), true
) {
    companion object {
        private val PRELUDE_TEXT = this::class.java.getResourceAsStream("/prelude/1.0.smithy")!!.readAllBytes()
            .toString(StandardCharsets.UTF_8)
        private val preludes = mutableMapOf<Project, SmithyFile>()
    }

    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve(): SmithyShape? {
        val declaredNamespace = myElement.declaredNamespace
        val enclosingNamespace = myElement.enclosingNamespace
        //Note: currently this can only resolve references between project source files
        FileTypeIndex.getFiles(SmithyFileType, GlobalSearchScope.allScope(myElement.project)).forEach { file ->
            val smithyFile = PsiManager.getInstance(myElement.project).findFile(file) as SmithyFile? ?: return@forEach
            smithyFile.model.shapes.forEach { shape ->
                //Note: for ambiguous shape ids (which lack a declared namespace), matching can fallback to the enclosing
                //namespace to match the model file merging logic of normal Smithy builds
                if (shape.name == myElement.name && (shape.namespace == declaredNamespace || (declaredNamespace == null && shape.namespace == enclosingNamespace))) {
                    return shape
                }
            }
        }
        //Attempt to find the shape within the bundled prelude
        if ((declaredNamespace == null || "smithy.api" == declaredNamespace)) {
            val prelude = preludes.getOrCreate(myElement.project) {
                PsiFileFactory.getInstance(it).createFileFromText(
                    "prelude.smithy", SmithyLanguage, PRELUDE_TEXT
                ).apply {
                    virtualFile.isWritable = false
                    PsiDocumentManager.getInstance(myElement.project).getDocument(this)?.setReadOnly(true)
                } as SmithyFile
            }
            return prelude.model.shapes.find { it.name == myElement.name }
        }
        return null
    }

    /**
     * A [PsiReference] from a [SmithyShapeName] which can resolve to its original [SmithyShape] using the parent [SmithyShapeId] or [SmithyShape] (for own references).
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ByName(shapeName: SmithyShapeName) : PsiReferenceBase<SmithyShapeName>(
        shapeName, TextRange.from(0, shapeName.textLength), true
    ) {
        private val ownRef = shapeName.parent as? SmithyShape
        private val delegate = (shapeName.parent as? SmithyShapeId)?.let { SmithyShapeReference(it) }
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve() = ownRef ?: delegate?.resolve()
    }

    /**
     * A [PsiReference] from a [SmithyEntry] which can resolve to its original [SmithyMember].
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ByMember(entry: SmithyEntry) : PsiReferenceBase<SmithyEntry>(
        entry, TextRange.from(0, entry.textLength), true
    ) {
        private val delegate = PsiTreeUtil.findFirstParent(entry) { it is SmithyTrait }?.let {
            SmithyShapeReference((it as SmithyTrait).shapeId)
        }

        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve() = (delegate?.resolve() as? SmithyAggregateShape)?.let { shape ->
            shape.body.members.find { it.name == myElement.name }
        }
    }

    /**
     * A [PsiReference] from a [SmithyKey] which can resolve to its original [SmithyMember] using the parent [SmithyEntry].
     *
     * @author Ian Caffey
     * @since 1.0
     */
    class ByKey(key: SmithyKey) : PsiReferenceBase<SmithyKey>(
        key, TextRange.from(0, key.textLength), true
    ) {
        private val delegate = (myElement.parent as? SmithyEntry)?.let { ByMember(it) }
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve() = delegate?.resolve()
    }
}