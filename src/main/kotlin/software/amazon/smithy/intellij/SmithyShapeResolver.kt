package software.amazon.smithy.intellij

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.search.GlobalSearchScope
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyShapeId

/**
 * A utility class providing methods to resolve the target of a [SmithyShapeId].
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyShapeResolver {
    fun shapeIdOf(target: PsiElement) = when (target) {
        is SmithyShape -> target.shapeId
        is SmithyExternalShape -> target.id
        else -> null
    }

    fun resolve(project: Project, id: String, exact: Boolean = true) = resolve(
        ResolveContext(project, id, id.split('#', limit = 2)[0], null, exact)
    )

    fun resolve(shapeId: SmithyShapeId) = resolve(
        ResolveContext(
            shapeId.project, shapeId.id, shapeId.enclosingNamespace, shapeId.declaredNamespace, true
        )
    )

    private fun resolve(ctx: ResolveContext): List<PsiElement> {
        val results = mutableListOf<PsiElement>()
        val shapeName = if (ctx.shapeId.contains('#')) ctx.shapeId.split('#', limit = 2)[1] else ctx.shapeId
        val manager = PsiManager.getInstance(ctx.project)
        val scope = GlobalSearchScope.allScope(ctx.project)
        //Attempt to find the shape within the project IDL
        SmithyFileIndex.forEach(scope) { file ->
            file.model?.shapes?.forEach { shape ->
                if (results.none { shape.shapeId == shapeIdOf(it) } && matches(ctx, shape.namespace, shape.name)) {
                    results.add(shape)
                }
            }
        }
        //Attempt to find the shape within any project AST
        SmithyAstIndex.forEach(scope) { ast, file ->
            ast.shapes?.forEach { (id, shape) ->
                val (namespace, name) = id.split('#', limit = 2)
                if (results.none { id == shapeIdOf(it) } && matches(ctx, namespace, name)) {
                    results.add(SmithyExternalShape(ast, manager.findFile(file)!!, id, shape))
                }
            }
        }
        //Attempt to find the shape within the bundled prelude
        val declaredNamespace = ctx.declaredNamespace
        if ("smithy.api" == declaredNamespace || (declaredNamespace == null && (!ctx.exact || results.isEmpty()))) {
            val prelude = SmithyPreludeIndex.getPrelude(ctx.project)
            prelude.model?.shapes?.forEach { shape ->
                val shapeId = "${shape.namespace}#${shape.name}"
                if (shape.name == shapeName && results.none { shapeId == shapeIdOf(shape) }) results.add(shape)
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
        val project: Project,
        val shapeId: String,
        val enclosingNamespace: String,
        val declaredNamespace: String?,
        val exact: Boolean
    ) {
        val shapeName = if (shapeId.contains('#')) shapeId.split('#', limit = 2)[1] else shapeId
    }
}

data class SmithyExternalShape(
    val ast: SmithyAst, val file: PsiFile, val id: String, val shape: SmithyAst.Shape
) : FakePsiElement() {
    val members = shape.let { it as? SmithyAst.AggregateShape }?.let {
        (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
            SmithyExternalMember(this, memberName, reference)
        }
    } ?: emptyList()

    override fun getName() = id.split('#', limit = 2)[1]
    override fun getParent() = file
    override fun getLocationString(): String = id.split('#', limit = 2)[0]
    override fun getIcon(unused: Boolean) = SmithyIcons.SHAPE
    override fun toString() = id
}

data class SmithyExternalMember(
    val parent: SmithyExternalShape, val memberName: String, val reference: SmithyAst.Reference
) : FakePsiElement() {
    override fun getName() = memberName
    override fun getParent(): PsiElement = parent
    override fun getLocationString(): String = parent.locationString
    override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
    override fun toString() = "$parent$$memberName"
}