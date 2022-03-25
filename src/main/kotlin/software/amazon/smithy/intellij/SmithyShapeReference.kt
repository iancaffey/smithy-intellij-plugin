package software.amazon.smithy.intellij

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyShapeId

/**
 * A [PsiReference] from a [SmithyShapeId] to its original [SmithyShape].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyShapeReference(shapeId: SmithyShapeId) : PsiReferenceBase<SmithyShapeId>(
    shapeId, TextRange.from(0, shapeId.textLength), true
) {
    override fun getAbsoluteRange(): TextRange = myElement.textRange
    override fun resolve(): PsiElement? {
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
        //Note: this could resolve to a prelude shape (which could be represented with a fake PsiElement for display in the IDE)
        return null
    }
}