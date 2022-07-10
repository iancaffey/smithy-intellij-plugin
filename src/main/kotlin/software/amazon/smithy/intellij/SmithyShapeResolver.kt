package software.amazon.smithy.intellij

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyAstShape
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyNamespace
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyShapeId

/**
 * A utility class providing methods to resolve the target of a [SmithyShapeId].
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyShapeResolver {
    fun resolve(member: SmithyMemberDefinition) = resolve(
        member.targetShapeId,
        member.containingFile
    )

    fun resolve(id: String, file: PsiFile, exact: Boolean = true): List<SmithyShapeDefinition> =
        if ('#' in id) {
            val declaredNamespace = id.split('#', limit = 2)[0]
            resolve(ResolveContext(id, file, declaredNamespace, declaredNamespace, exact))
        } else {
            val enclosingNamespace = (file as? SmithyFile)?.let {
                PsiTreeUtil.getChildOfType(it.model, SmithyNamespace::class.java)?.namespaceId?.id
            }
            resolve(ResolveContext(id, file, enclosingNamespace, null, exact))
        }

    fun resolve(shapeId: SmithyShapeId) = resolve(
        ResolveContext(
            shapeId.id,
            shapeId.containingFile,
            shapeId.enclosingNamespace,
            shapeId.declaredNamespace
        )
    )

    private fun resolve(ctx: ResolveContext): List<SmithyShapeDefinition> {
        val results = mutableListOf<SmithyShapeDefinition>()
        val manager = PsiManager.getInstance(ctx.enclosingFile.project)
        val scope = GlobalSearchScope.allScope(ctx.enclosingFile.project)
        //Attempt to find the shape within the project IDL
        SmithyFileIndex.forEach(scope) { file ->
            file.model?.shapes?.forEach { shape ->
                if (results.none { shape.shapeId == it.shapeId } && matches(ctx, shape.namespace, shape.name)) {
                    results.add(shape)
                }
            }
        }
        //Attempt to find the shape within any project AST
        SmithyAstIndex.forEach(scope) { ast, file ->
            ast.shapes?.forEach { (id, shape) ->
                val (namespace, name) = id.split('#', limit = 2)
                if (results.none { id == it.shapeId } && matches(ctx, namespace, name)) {
                    results.add(SmithyAstShape(ast, manager.findFile(file)!!, id, shape))
                }
            }
        }
        //Attempt to find the shape within the prelude by name, if not explicitly scoped to a namespace and no previous matches were found
        if (ctx.exact && ctx.declaredNamespace == null && results.isEmpty()) {
            SmithyFileIndex.forEach(scope) { file ->
                file.model?.shapes?.forEach { shape ->
                    if (shape.namespace == SmithyPreludeShapes.NAMESPACE && shape.name == ctx.shapeName && results.none { shape.shapeId == it.shapeId }) {
                        results.add(shape)
                    }
                }
            }
        }
        return results
    }

    private fun matches(ctx: ResolveContext, namespace: String, name: String): Boolean {
        if (!ctx.exact) return name == ctx.shapeName
        val declaredNamespace = ctx.declaredNamespace
        val enclosingNamespace = ctx.enclosingNamespace
        //Note: for ambiguous shape ids (which lack a declared namespace), matching can fallback to the enclosing
        //namespace to match the model file merging logic of normal Smithy builds
        return name == ctx.shapeName && (namespace == declaredNamespace || (declaredNamespace == null && namespace == enclosingNamespace))
    }

    private data class ResolveContext(
        val shapeId: String,
        val enclosingFile: PsiFile,
        val enclosingNamespace: String?,
        val declaredNamespace: String?,
        val exact: Boolean = true
    ) {
        val shapeName = if (shapeId.contains('#')) shapeId.split('#', limit = 2)[1] else shapeId
    }
}
