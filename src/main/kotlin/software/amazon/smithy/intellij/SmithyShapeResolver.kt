package software.amazon.smithy.intellij

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager.getCachedValue
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import software.amazon.smithy.intellij.index.SmithyAstShapeIndex
import software.amazon.smithy.intellij.index.SmithyDefinedShapeIdIndex
import software.amazon.smithy.intellij.index.SmithyShapeNameResolutionHintIndex
import software.amazon.smithy.intellij.psi.SmithyMetadata
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyTraitDefinition

/**
 * A utility class providing methods to resolve the namespace of a shape name (relative shape id).
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyShapeResolver {
    private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)

    fun getDefinitions(shapeId: SmithyShapeId): List<SmithyShapeDefinition> =
        getCachedValue(shapeId) {
            val namespace = shapeId.resolvedNamespace
            val definitions =
                namespace?.let { getDefinitions(it, shapeId.shapeName, shapeId.resolveScope) } ?: emptyList()
            CachedValueProvider.Result.create(definitions, dependencies)
        }

    fun getDefinitions(trait: SmithyTraitDefinition): List<SmithyShapeDefinition> =
        getCachedValue(trait) {
            val namespace = trait.resolvedNamespace
            val definitions = namespace?.let { getDefinitions(it, trait.shapeName, trait.resolveScope) } ?: emptyList()
            CachedValueProvider.Result.create(definitions, dependencies)
        }

    fun getDefinitions(context: PsiElement, namespace: String, shapeName: String): List<SmithyShapeDefinition> =
        getCachedValue(context) {
            CachedValueProvider.Result.create(getDefinitions(namespace, shapeName, context.resolveScope), dependencies)
        }

    fun getDefinitions(namespace: String, shapeName: String, scope: GlobalSearchScope): List<SmithyShapeDefinition> {
        val project = scope.project ?: return emptyList()
        val psi = PsiManager.getInstance(project)
        val definitions = mutableListOf<SmithyShapeDefinition>()
        definitions += SmithyAstShapeIndex.getShapes(namespace, shapeName, scope)
        SmithyDefinedShapeIdIndex.getFiles(namespace, shapeName, scope).forEach { file ->
            if (file.extension != "smithy") return@forEach
            (psi.findFile(file) as? SmithyFile)?.model?.shapes?.forEach { shape ->
                if (shape.shapeName == shapeName && shape.namespace == namespace) {
                    definitions += shape
                }
            }
        }
        return definitions
    }

    fun getNamespace(element: PsiElement, shapeName: String): String? {
        //Note: relative shapes within metadata can only refer to prelude shapes (even if imported later on in the file)
        getParentOfType(element, SmithyMetadata::class.java)?.let { return "smithy.api" }
        val scope = element.resolveScope
        val hint = SmithyShapeNameResolutionHintIndex.getHint(shapeName, element.containingFile)
        return when {
            hint == null -> null //Note: only happens when the project hasn't completed indexing
            hint.resolvedNamespace != null -> hint.resolvedNamespace
            SmithyDefinedShapeIdIndex.exists(hint.enclosingNamespace, shapeName, scope) -> hint.enclosingNamespace
            SmithyDefinedShapeIdIndex.exists("smithy.api", shapeName, scope) -> "smithy.api"
            else -> null
        }
    }
}
