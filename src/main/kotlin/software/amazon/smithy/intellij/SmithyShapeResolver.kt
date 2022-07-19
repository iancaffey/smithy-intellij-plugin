package software.amazon.smithy.intellij

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager.getCachedValue
import com.intellij.psi.util.PsiModificationTracker
import software.amazon.smithy.intellij.index.SmithyAstShapeIndex
import software.amazon.smithy.intellij.index.SmithyDefinedShapeIdIndex
import software.amazon.smithy.intellij.index.SmithyShapeNameResolutionHintIndex
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
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
    fun getDefinitions(member: SmithyMemberDefinition): List<SmithyShapeDefinition> =
        getCachedValue(member) {
            val namespace = member.resolvedTargetNamespace
            val definitions =
                namespace?.let { getDefinitions(it, member.targetShapeName, member.project) } ?: emptyList()
            CachedValueProvider.Result.create(definitions, dependencies)
        }

    fun getDefinitions(shapeId: SmithyShapeId): List<SmithyShapeDefinition> =
        getCachedValue(shapeId) {
            val namespace = shapeId.resolvedNamespace
            val definitions = namespace?.let { getDefinitions(it, shapeId.shapeName, shapeId.project) } ?: emptyList()
            CachedValueProvider.Result.create(definitions, dependencies)
        }

    fun getDefinitions(trait: SmithyTraitDefinition): List<SmithyShapeDefinition> =
        getCachedValue(trait) {
            val namespace = trait.resolvedNamespace
            val definitions = namespace?.let { getDefinitions(it, trait.shapeName, trait.project) } ?: emptyList()
            CachedValueProvider.Result.create(definitions, dependencies)
        }

    fun getNamespace(scope: PsiElement, shapeName: String): String? = getCachedValue(scope) {
        CachedValueProvider.Result.create(getNamespace(scope.containingFile, shapeName), dependencies)
    }

    fun getDefinitions(namespace: String, shapeName: String, project: Project): List<SmithyShapeDefinition> {
        val psi = PsiManager.getInstance(project)
        val definitions = mutableListOf<SmithyShapeDefinition>()
        definitions += SmithyAstShapeIndex.getShapes(namespace, shapeName, project)
        SmithyDefinedShapeIdIndex.getFiles(namespace, shapeName, project).forEach { file ->
            if (file.extension != "smithy") return@forEach
            (psi.findFile(file) as? SmithyFile)?.model?.shapes?.forEach { shape ->
                if (shape.shapeName == shapeName && shape.namespace == namespace) {
                    definitions += shape
                }
            }
        }
        return definitions
    }

    private fun getNamespace(enclosingFile: PsiFile, shapeName: String): String? {
        val project = enclosingFile.project
        val hint = SmithyShapeNameResolutionHintIndex.getHint(shapeName, enclosingFile)
        return when {
            hint == null -> null //Note: only happens when the project hasn't completed indexing
            hint.resolvedNamespace != null -> hint.resolvedNamespace
            SmithyDefinedShapeIdIndex.exists(hint.enclosingNamespace, shapeName, project) -> hint.enclosingNamespace
            SmithyDefinedShapeIdIndex.exists("smithy.api", shapeName, project) -> "smithy.api"
            else -> null
        }
    }
}
