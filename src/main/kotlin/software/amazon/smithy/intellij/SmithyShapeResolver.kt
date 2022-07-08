package software.amazon.smithy.intellij

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyNamespace
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
                    results.add(SmithyExternalShape(ast, manager.findFile(file)!!, id, shape))
                }
            }
        }
        //Attempt to find the shape within the prelude by name, if not explicitly scoped to a namespace and no previous matches were found
        if (ctx.exact && ctx.declaredNamespace == null && results.isEmpty()) {
            SmithyFileIndex.forEach(scope) { file ->
                file.model?.shapes?.forEach { shape ->
                    if (shape.namespace == "smithy.api" && shape.name == ctx.shapeName && results.none { shape.shapeId == it.shapeId }) {
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

data class SmithyExternalShape(
    val ast: SmithyAst, val file: PsiFile, override val shapeId: String, val shape: SmithyAst.Shape
) : FakePsiElement(), SmithyShapeDefinition {
    override val type = shape.type
    override val members = shape.let { it as? SmithyAst.AggregateShape }?.let {
        (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
            SmithyExternalMember(this, memberName, reference)
        }
    } ?: emptyList()
    override val namespace = shapeId.split('#', limit = 2)[0]
    override fun getName() = shapeId.split('#', limit = 2)[1]
    override fun hasTrait(id: String) = shape.traits?.keys?.any { it == id } ?: false
    override fun getMember(name: String): SmithyExternalMember? {
        val key = if (shape is SmithyAst.Map) "value" else name
        return members.find { it.name == key }
    }

    override fun getParent() = file
    override fun getLocationString() = namespace
    override fun getIcon(unused: Boolean) = SmithyIcons.SHAPE
    override fun toString() = shapeId
}

data class SmithyExternalMember(
    override val enclosingShape: SmithyExternalShape, val memberName: String, val reference: SmithyAst.Reference
) : FakePsiElement(), SmithyMemberDefinition {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(member: SmithyExternalMember) = CachedValueProvider {
            val results = SmithyShapeResolver.resolve(member)
            CachedValueProvider.Result.create(if (results.size == 1) results.first() else null, dependencies)
        }
    }

    override val targetShapeId = reference.target
    override fun getName() = memberName
    override fun getParent(): SmithyExternalShape = enclosingShape
    override fun getLocationString(): String = enclosingShape.locationString
    override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
    override fun resolve(): SmithyShapeDefinition? = CachedValuesManager.getCachedValue(this, resolver(this))
    override fun toString() = "$parent$$memberName"
}