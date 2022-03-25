package software.amazon.smithy.intellij

import com.intellij.openapi.util.TextRange
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
    override fun resolve(): SmithyShape? {
        //Note: currently this can only resolve references between project source files
        FileTypeIndex.getFiles(SmithyFileType, GlobalSearchScope.allScope(myElement.project)).forEach { file ->
            val smithyFile = PsiManager.getInstance(myElement.project).findFile(file) as SmithyFile? ?: return@forEach
            smithyFile.model.shapes.forEach { shape ->
                if (shape.name == myElement.name && shape.namespace == myElement.namespace) {
                    return shape
                }
            }
        }
        return null
    }
}