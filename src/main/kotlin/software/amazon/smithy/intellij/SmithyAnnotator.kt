package software.amazon.smithy.intellij

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import com.intellij.psi.util.nextLeafs
import software.amazon.smithy.intellij.psi.SmithyControl
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyIncompleteAppliedTrait
import software.amazon.smithy.intellij.psi.SmithyIncompleteEntry
import software.amazon.smithy.intellij.psi.SmithyIncompleteMember
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyMap
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyMemberName
import software.amazon.smithy.intellij.psi.SmithyModel
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyStatement
import software.amazon.smithy.intellij.psi.SmithyString
import software.amazon.smithy.intellij.psi.SmithyTextBlock
import software.amazon.smithy.intellij.psi.SmithyTrait
import software.amazon.smithy.intellij.psi.SmithyTypes
import java.util.*

/**
 * An [Annotator] which provides annotations to [Smithy](https://awslabs.github.io/smithy) model files.
 *
 * All syntax checks which require the higher-level AST (resulting from a complete parse) are handled to complete syntax highlighting from [SmithySyntaxHighlighter].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAnnotator : Annotator {
    private val annotations = EnumSet.allOf(Annotation::class.java)
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        annotations.forEach { it.annotate(element, holder) }
    }
}

private enum class Annotation : Annotator {
    KEYWORD {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            getParentOfType(element, SmithyStatement::class.java)?.takeIf { element == it.typeIdentifier }?.let {
                holder.highlight(SmithyColorSettings.KEYWORD)
            }
        }
    },
    CONTROL {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if ((element is SmithyKey || element.elementType == SmithyTypes.TOKEN_DOLLAR_SIGN) && element.parent is SmithyControl) {
                holder.highlight(SmithyColorSettings.CONTROL)
            }
        }
    },
    KEY {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyKey && element.parent !is SmithyControl) {
                holder.highlight(SmithyColorSettings.KEY)
            }
        }
    },
    MEMBER {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyMemberName) {
                holder.highlight(SmithyColorSettings.SHAPE_MEMBER)
            }
        }
    },
    TRAIT_NAME {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if ((element is SmithyShapeId || element.elementType == SmithyTypes.TOKEN_AT) && element.parent is SmithyTrait) {
                holder.highlight(SmithyColorSettings.TRAIT_NAME)
            }
        }
    },
    ESCAPE_SEQUENCES {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if ((element is SmithyString || element is SmithyTextBlock) && element.text.contains("\\")) {
                val valid = mutableListOf<TextRange>()
                val invalid = mutableListOf<TextRange>()
                var i = 0
                while (i < element.textLength) {
                    if (element.text[i] != '\\') {
                        i++
                        continue
                    }
                    val length = if (i < element.textLength - 5 && element.text[i + 1] == 'u' && (2..5).all {
                            val digit = element.text[i + it]
                            (digit in '0'..'9') || (digit in 'a'..'f') || (digit in 'A'..'F')
                        }) 5 else 2
                    if (length == 2 && element.text[i + 1] !in "\"'bfnrt/\\\n") {
                        invalid.add(TextRange.from(i, length))
                    } else {
                        valid.add(TextRange.from(i, length))
                    }
                    i += length
                }
                val range = element.textRange
                valid.forEach { holder.highlight(SmithyColorSettings.VALID_ESCAPE_SEQUENCE, range.cutOut(it)) }
                invalid.forEach {
                    holder.highlight(
                        HighlightSeverity.ERROR,
                        "Invalid escape sequence: '${it.substring(element.text)}'",
                        SmithyColorSettings.INVALID_ESCAPE_SEQUENCE,
                        range.cutOut(it)
                    )
                }
            }
        }
    },
    TRAILING_NEW_LINE {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element.elementType in SmithyFormattingModelBuilder.TOKENS_REQUIRING_TRAILING_NEW_LINE && element.nextLeaf() != null) {
                val trailingWhiteSpace = element.nextLeafs.takeWhile { it is PsiComment || it is PsiWhiteSpace }
                if (trailingWhiteSpace.none { it.textContains('\n') }) {
                    holder.highlight(HighlightSeverity.ERROR, "Expecting trailing line break '\\n'")
                }
            }
        }
    },
    INCOMPLETE_STRING {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element.elementType == SmithyTypes.TOKEN_INCOMPLETE_STRING) {
                holder.highlight(HighlightSeverity.ERROR, "Expecting closing quote '\"'")
            }
            if (element.elementType == SmithyTypes.TOKEN_INCOMPLETE_TEXT_BLOCK) {
                holder.highlight(HighlightSeverity.ERROR, "Expecting closing quotes '\"\"\"'")
            }
        }
    },
    INCOMPLETE_TRAIT {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyIncompleteAppliedTrait) {
                holder.highlight(HighlightSeverity.ERROR, "Missing trait")
            }
        }
    },
    INCOMPLETE_MEMBER {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyIncompleteEntry || element is SmithyIncompleteMember) {
                holder.highlight(HighlightSeverity.ERROR, "Missing shape id")
            }
        }
    },
    MISSING_MEMBERS {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyShape) {
                val missingMembers = element.requiredMembers.filter { name -> element.members.none { it.name == name } }
                val message = when (missingMembers.size) {
                    0 -> null
                    1 -> "Missing '${missingMembers.first()}'"
                    2 -> "Missing '${missingMembers[0]}' and '${missingMembers[1]}'"
                    else -> missingMembers.subList(0, missingMembers.size - 1).joinToString(
                        ", ", "Missing ", ", and '${missingMembers.last()}'"
                    ) { "'$it'" }
                }
                if (message != null) {
                    holder.highlight(HighlightSeverity.ERROR, message)
                }
            }
        }
    },
    UNSUPPORTED_MEMBER {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            when (element) {
                is SmithyMember -> element.name to element.parent //all normal shapes
                is SmithyEntry -> element.name to element.parent.parent //service, resource, operation
                else -> null
            }?.takeIf { (_, parent) -> parent is SmithyShape }?.let { (name, parent) ->
                val enclosingShape = parent as SmithyShape
                enclosingShape.supportedMembers?.toList()?.let {
                    if (name !in it) {
                        val message = when (it.size) {
                            0 -> "${enclosingShape.type} does not have members"
                            1 -> "${enclosingShape.type} expects '${it.first()}'"
                            2 -> "${enclosingShape.type} expects '${it[0]}' or '${it[1]}'"
                            else -> it.subList(0, it.size - 1).joinToString(
                                ", ", "${enclosingShape.type} expects ", ", or '${it.last()}'"
                            ) { member -> "'$member'" }
                        }
                        holder.highlight(HighlightSeverity.ERROR, message)
                    }
                }
            }
        }
    },
    DUPLICATE_MEMBER {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyMember && element.parent.children.any {
                    it is SmithyMember && it != element && it.name == element.name
                }) {
                holder.newAnnotation(HighlightSeverity.ERROR, "'${element.name}' is already defined")
                    .withFix(SmithyRemoveMemberQuickFix(element))
                    .create()
            }
        }
    },
    MAP_KEY_STRING_TARGET {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyMember && element.name == "key" && element.enclosingShape is SmithyMap) {
                val target = element.resolve()
                if (target != null && target.type != "string") {
                    holder.highlight(HighlightSeverity.ERROR, "'key' must target a string shape")
                }
            }
        }
    },
    INVALID_TRAIT {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyTrait) {
                val target = element.resolve()
                if (target != null && !target.hasTrait(SmithyPreludeShapes.TRAIT)) {
                    holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "${element.shape.shapeName} cannot be used as a trait"
                    ).range(element.shape).highlightType(ProblemHighlightType.ERROR).create()
                }
            }
        }
    },
    IMPORT_OPTIMIZER {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyShapeId && element.parent !is SmithyImport) {
                element.namespaceId?.let { namespaceId ->
                    val conflicts = getParentOfType(element, SmithyModel::class.java)?.shapes?.any {
                        it.name == element.shapeName
                    } == true
                    if (!conflicts) {
                        val fix = SmithyOptimizeShapeIdQuickFix(element.project, element)
                        holder.newAnnotation(
                            HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING,
                            if (fix.hasImport) "Remove unnecessary qualifier" else "Add import for: ${element.shapeName}"
                        ).range(namespaceId.textRange)
                            .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                            .withFix(fix)
                            .create()
                    }
                }
            }
        }
    },
    IMPORT_CONFLICT {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyImport && getParentOfType(element, SmithyModel::class.java)?.shapes?.any {
                    it.name == element.shapeId.shapeName
                } == true) {
                holder.newAnnotation(HighlightSeverity.ERROR, "'${element.shapeId.shapeName}' is already defined")
                    .withFix(SmithyRemoveImportQuickFix(element))
                    .create()
            }
        }
    },
    UNUSED_IMPORT {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            if (element is SmithyImport && element in SmithyImportOptimizer.unusedImports(element.containingFile)) {
                holder.newAnnotation(HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING, "Unused import")
                    .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                    .withFix(SmithyRemoveUnusedImportsQuickFix)
                    .create()
            }
        }
    },
    REFERENCE_INSPECTION {
        override fun annotate(element: PsiElement, holder: AnnotationHolder) {
            when (val reference = element.reference) {
                is SmithyKeyReference -> {
                    if (!reference.isSoft && reference.resolve() == null) {
                        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved member: ${reference.key.text}")
                            .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                            .create()
                    }
                }
                is SmithyMemberReference -> {
                    if (!reference.isSoft && reference.resolve() == null) {
                        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved member: ${reference.id.text}")
                            .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                            .create()
                    }
                }
                is SmithyShapeReference -> {
                    val target = reference.resolve()
                    if (target == null) {
                        if (!reference.isSoft) {
                            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved shape: ${element.text}")
                                .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                                .also {
                                    if (element is SmithyShapeId) {
                                        it.withFix(SmithyImportShapeQuickFix(element.text, element.containingFile))
                                    }
                                }
                                .create()
                        }
                    } else if (element is SmithyShapeId) {
                        val enclosingNamespace = (element.containingFile as? SmithyFile)?.model?.namespace
                        if (target.namespace != enclosingNamespace && target.hasTrait(SmithyPreludeShapes.PRIVATE)) {
                            holder.newAnnotation(
                                HighlightSeverity.ERROR,
                                "${element.shapeName} cannot be referenced outside ${target.namespace}"
                            ).highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL).create()
                        }
                        if (target.hasTrait(SmithyPreludeShapes.DEPRECATED)) {
                            holder.newAnnotation(
                                HighlightSeverity.WARNING,
                                "${element.shapeName} is marked as @deprecated and could be removed in the future"
                            ).highlightType(ProblemHighlightType.LIKE_DEPRECATED).create()
                        }
                        if (target.hasTrait(SmithyPreludeShapes.UNSTABLE)) {
                            holder.newAnnotation(
                                HighlightSeverity.WARNING,
                                "${element.shapeName} is marked as @unstable and could change in the future"
                            ).highlightType(ProblemHighlightType.WARNING).create()
                        }
                    }
                }
            }
        }
    }
}

private fun AnnotationHolder.highlight(key: TextAttributesKey) =
    newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(key).create()

private fun AnnotationHolder.highlight(key: TextAttributesKey, range: TextRange) =
    newSilentAnnotation(HighlightSeverity.INFORMATION).range(range).textAttributes(key).create()

private fun AnnotationHolder.highlight(severity: HighlightSeverity, message: String) =
    newAnnotation(severity, message).create()

private fun AnnotationHolder.highlight(
    severity: HighlightSeverity, message: String, range: TextRange
) = newAnnotation(severity, message).range(range).create()

private fun AnnotationHolder.highlight(
    severity: HighlightSeverity, message: String, key: TextAttributesKey, range: TextRange
) = newAnnotation(severity, message).textAttributes(key).range(range).create()