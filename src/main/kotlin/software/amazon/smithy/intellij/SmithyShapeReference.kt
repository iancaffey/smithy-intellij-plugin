package software.amazon.smithy.intellij

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyAggregateShape
import software.amazon.smithy.intellij.psi.SmithyArray
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMap
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

        fun shapeIdOf(target: PsiElement) = when (target) {
            is SmithyShape -> target.shapeId
            is ExternalShape -> target.id
            else -> null
        }

        fun resolve(project: Project, id: String, exact: Boolean = true) = resolve(
            ResolveContext(project, id, id.split('#', limit = 2)[0], null, exact)
        )

        fun resolve(shapeId: SmithyShapeId) = resolve(
            ResolveContext(shapeId.project, shapeId.id, shapeId.enclosingNamespace, shapeId.declaredNamespace, true)
        )

        fun resolve(ctx: ResolveContext): List<PsiElement> {
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
                        results.add(ExternalShape(ast, manager.findFile(file)!!, id, shape))
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

    }

    abstract val id: String?
    abstract val parent: PsiElement?
    override fun isSoft() = parent == null

    data class ResolveContext(
        val project: Project,
        val shapeId: String,
        val enclosingNamespace: String,
        val declaredNamespace: String?,
        val exact: Boolean
    ) {
        val shapeName = if (shapeId.contains('#')) shapeId.split('#', limit = 2)[1] else shapeId
    }

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
                val results = resolve(shapeId)
                CachedValueProvider.Result.create(if (results.size == 1) results.first() else null, dependencies)
            }
        }

        override val id = shapeId.id
        override val parent: PsiElement? = shapeId.parent
        override fun handleElementRename(newElementName: String) = myElement.setName(newElementName)
        override fun getAbsoluteRange(): TextRange = myElement.textRange
        override fun resolve(): PsiElement? = CachedValuesManager.getCachedValue(myElement, resolver(myElement))
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
        override fun resolve() = if (path != null) delegate?.resolve()?.let {
            path.find(it)
        } else null

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
                    override fun find(shape: ExternalShape): PsiElement? {
                        val target = if (shape.shape is SmithyAst.Map) "value" else name
                        return shape.members.find { it.name == target }
                    }

                    override fun find(shape: SmithyAggregateShape): PsiElement? {
                        val target = if (shape is SmithyMap) "value" else name
                        return shape.body.members.find { it.name == target }
                    }

                    override fun toString() = name
                }
            }

            fun find(shape: ExternalShape): PsiElement?
            fun find(shape: SmithyAggregateShape): PsiElement?
            fun find(element: PsiElement) = when (element) {
                is SmithyAggregateShape -> find(element)
                is ExternalShape -> find(element)
                else -> null
            }

            fun andThen(next: MemberPath) = object : MemberPath {
                override fun find(shape: ExternalShape) = this@MemberPath.find(shape)?.let {
                    next.find(resolveShape(it))
                }

                override fun find(shape: SmithyAggregateShape) = this@MemberPath.find(shape)?.let {
                    next.find(resolveShape(it))
                }

                override fun toString() = "${this@MemberPath}.$next"

                private fun resolveShape(element: PsiElement): PsiElement {
                    val resolved = when (element) {
                        is SmithyMember -> resolve(element.shapeId)
                        is ExternalShapeMember -> resolve(element.project, element.reference.target)
                        else -> emptyList()
                    }
                    return if (resolved.size == 1) resolved.first() else element
                }
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

    data class ExternalShape(
        val ast: SmithyAst, val file: PsiFile, val id: String, val shape: SmithyAst.Shape
    ) : FakePsiElement() {
        val members = shape.let { it as? SmithyAst.AggregateShape }?.let {
            (it.members ?: emptyMap()).entries.map { (memberName, reference) ->
                ExternalShapeMember(this, memberName, reference)
            }
        } ?: emptyList()

        override fun getName() = id.split('#', limit = 2)[1]
        override fun getParent() = file
        override fun getLocationString(): String = id.split('#', limit = 2)[0]
        override fun getIcon(unused: Boolean) = SmithyIcons.SHAPE
        override fun toString() = id
    }

    data class ExternalShapeMember(
        val parent: ExternalShape, val memberName: String, val reference: SmithyAst.Reference
    ) : FakePsiElement() {
        override fun getName() = memberName
        override fun getParent(): PsiElement = parent
        override fun getLocationString(): String = parent.locationString
        override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
        override fun toString() = "$parent$$memberName"
    }
}