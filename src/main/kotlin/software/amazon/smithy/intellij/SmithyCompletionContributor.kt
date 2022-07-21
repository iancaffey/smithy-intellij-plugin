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
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import com.intellij.util.ProcessingContext
import com.intellij.util.applyIf
import software.amazon.smithy.intellij.index.SmithyDefinedShapeIdIndex
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyIncompleteEntry
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMemberId
import software.amazon.smithy.intellij.psi.SmithyObject
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
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
                    getParentOfType(element, SmithyShapeId::class.java)?.let { addShapes(it, results) }
                    getParentOfType(element, SmithyEntry::class.java)?.let { addMembers(it, it.key, results) }
                    getParentOfType(element, SmithyIncompleteEntry::class.java)?.let { addMembers(it, it.key, results) }
                    getParentOfType(element, SmithyMemberId::class.java)?.let {
                        val parent = it.shapeId.reference.resolve()
                        parent?.members?.forEach { member ->
                            results.addElement(memberElement(member.name, member.targetShapeName, parent))
                        }
                    }
                }
            })
    }
}

private fun addShapes(element: PsiElement, results: CompletionResultSet) {
    val addImports = getParentOfType(element, SmithyImport::class.java) == null
    SmithyDefinedShapeIdIndex.forEach(element.resolveScope) {
        val (namespace, shapeName) = it.split('#', limit = 2)
        results.addElement(shapeElement(namespace, shapeName, addImports))
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
        memberResults.addElement(memberElement(member.name, member.targetShapeName))
    }
}

private fun shapeElement(namespace: String, shapeName: String, addImports: Boolean): LookupElementBuilder {
    val qualifiedName = "$namespace#$shapeName"
    return LookupElementBuilder.create(if (addImports) shapeName else qualifiedName)
        .withIcon(SmithyIcons.SHAPE)
        .withPresentableText(shapeName)
        .withLookupString(shapeName)
        .withLookupString(qualifiedName)
        .withTailText("($namespace)")
        .applyIf(addImports) {
            withInsertHandler { context, _ ->
                context.file.let { it as? SmithyFile }?.let {
                    val model = it.model ?: return@let
                    val enclosingNamespace = model.namespace ?: return@let
                    if (enclosingNamespace == namespace) return@let
                    if (namespace == "smithy.api"
                        && !SmithyDefinedShapeIdIndex.exists(enclosingNamespace, shapeName, it.resolveScope)
                    ) return@let
                    SmithyElementFactory.addImport(it, namespace, shapeName)
                }
            }
        }
}

private fun memberElement(
    memberName: String,
    shapeName: String,
    enclosingShape: SmithyShapeDefinition? = null
): LookupElementBuilder {
    return LookupElementBuilder.create(if (enclosingShape != null) "${enclosingShape.shapeId}$$memberName" else memberName)
        .withPresentableText(memberName)
        .withTypeText(shapeName)
        .withIcon(SmithyIcons.MEMBER)
}