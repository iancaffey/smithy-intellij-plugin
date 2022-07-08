package software.amazon.smithy.intellij

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import software.amazon.smithy.intellij.psi.SmithyAggregateShape
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * A [CompletionContributor] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyCompletionContributor : CompletionContributor() {
    companion object {
        fun shapeElement(namespace: String, name: String) =
            LookupElementBuilder.create("$namespace#$name", name).withIcon(SmithyIcons.SHAPE)
                .withTailText("($namespace)").withInsertHandler { context, _ ->
                    context.file.let { it as? SmithyFile }?.let {
                        val model = it.model ?: return@let
                        if (model.namespace == namespace) return@let
                        if (namespace == "smithy.api"
                            && SmithyShapeResolver.resolve("${model.namespace}#$name", it).isNotEmpty()
                        ) return@let
                        SmithyElementFactory.addImport(it, "$namespace#$name")
                    }
                }

        fun memberElement(shapeId: String, name: String) =
            LookupElementBuilder.create("$shapeId$$name", name).withTailText("($shapeId)").withIcon(SmithyIcons.MEMBER)
    }

    init {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement(SmithyTypes.TOKEN_SYMBOL),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {
                    val project = parameters.editor.project!!
                    val scope = GlobalSearchScope.allScope(project)
                    val addFromFile = { it: SmithyFile ->
                        it.model?.shapes?.forEach { shape ->
                            result.addElement(shapeElement(shape.namespace, shape.name))
                            if (shape is SmithyAggregateShape) {
                                shape.body.members.forEach { member ->
                                    result.addElement(
                                        memberElement(shape.shapeId, member.name)
                                    )
                                }
                            }
                        }
                    }
                    SmithyFileIndex.forEach(scope) { addFromFile(it) }
                    SmithyAstIndex.forEach(scope) { ast, _ ->
                        ast.shapes?.forEach { (id, shape) ->
                            val (namespace, name) = id.split('#', limit = 2)
                            result.addElement(shapeElement(namespace, name))
                            if (shape is SmithyAst.AggregateShape) {
                                shape.members?.forEach {
                                    result.addElement(memberElement(id, it.key))
                                }
                            }
                        }
                    }
                }
            })
    }
}