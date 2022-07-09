package software.amazon.smithy.intellij

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import com.intellij.util.ProcessingContext
import com.intellij.util.applyIf
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyIncompleteEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMemberId
import software.amazon.smithy.intellij.psi.SmithyObject
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyTrait
import software.amazon.smithy.intellij.psi.SmithyTraitBody
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [CompletionContributor] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement(SmithyTypes.TOKEN_SYMBOL),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, results: CompletionResultSet
                ) {
                    val element = parameters.originalPosition ?: return
                    if (getParentOfType(element, PsiErrorElement::class.java) != null) return
                    getParentOfType(element, SmithyShapeId::class.java)?.let { addShapes(it, parameters, results) }
                    getParentOfType(element, SmithyEntry::class.java)?.let { addMembers(it, it.key, results) }
                    getParentOfType(element, SmithyIncompleteEntry::class.java)?.let { addMembers(it, it.key, results) }
                    getParentOfType(element, SmithyMemberId::class.java)?.let {
                        it.shapeId.reference.resolve()?.members?.forEach { member ->
                            results.addElement(memberElement(member.name, member.targetShapeId, it.shapeId.id))
                        }
                    }
                }
            })
    }
}

private fun addShapes(element: PsiElement, parameters: CompletionParameters, results: CompletionResultSet) {
    val addImports = getParentOfType(element, SmithyImport::class.java) == null
    val project = parameters.editor.project!!
    val scope = GlobalSearchScope.allScope(project)
    SmithyFileIndex.forEach(scope) {
        it.model?.shapes?.forEach { shape ->
            results.addElement(shapeElement(shape.namespace, shape.name, addImports))
        }
    }
    SmithyAstIndex.forEach(scope) { ast, _ ->
        ast.shapes?.forEach { (id, _) ->
            val (namespace, name) = id.split('#', limit = 2)
            results.addElement(shapeElement(namespace, name, addImports))
        }
    }
}

private fun addMembers(element: PsiElement, key: SmithyKey, results: CompletionResultSet) {
    val memberResults = results.withPrefixMatcher(key.text)
    val enclosingShape = when (val parent = element.parent) {
        is SmithyObject -> parent.reference.resolve()
        is SmithyTraitBody -> (parent.parent as? SmithyTrait)?.resolve()
        else -> null
    }
    enclosingShape?.members?.forEach { member ->
        memberResults.addElement(memberElement(member.name, member.targetShapeId))
    }
}

private fun shapeElement(namespace: String, name: String, addImports: Boolean): LookupElementBuilder {
    val qualifiedName = "$namespace#$name"
    return LookupElementBuilder.create(if (addImports) name else qualifiedName)
        .withIcon(SmithyIcons.SHAPE)
        .withPresentableText(name)
        .withLookupString(name)
        .withLookupString(qualifiedName)
        .withTailText("($namespace)")
        .applyIf(addImports) {
            withInsertHandler { context, _ ->
                context.file.let { it as? SmithyFile }?.let {
                    val model = it.model ?: return@let
                    if (model.namespace == namespace) return@let
                    if (namespace == "smithy.api"
                        && SmithyShapeResolver.resolve("${model.namespace}#$name", it).isEmpty()
                    ) return@let
                    SmithyElementFactory.addImport(it, "$namespace#$name")
                }
            }
        }
}

private fun memberElement(
    memberName: String,
    shapeId: String,
    enclosingShapeId: String? = null
): LookupElementBuilder {
    val shapeName = if ('#' in shapeId) shapeId.split('#', limit = 2)[1] else shapeId
    return LookupElementBuilder.create(if (enclosingShapeId != null) "$enclosingShapeId$$memberName" else memberName)
        .withPresentableText(memberName)
        .withTypeText(shapeName)
        .withIcon(SmithyIcons.MEMBER)
}