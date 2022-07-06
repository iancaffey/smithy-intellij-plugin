package software.amazon.smithy.intellij

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import com.intellij.psi.util.nextLeafs
import software.amazon.smithy.intellij.psi.SmithyBoolean
import software.amazon.smithy.intellij.psi.SmithyControl
import software.amazon.smithy.intellij.psi.SmithyId
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyIncompleteEntry
import software.amazon.smithy.intellij.psi.SmithyIncompleteMember
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyKeyword
import software.amazon.smithy.intellij.psi.SmithyMemberName
import software.amazon.smithy.intellij.psi.SmithyNull
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithySimpleTypeName
import software.amazon.smithy.intellij.psi.SmithyString
import software.amazon.smithy.intellij.psi.SmithyTextBlock
import software.amazon.smithy.intellij.psi.SmithyTrait
import software.amazon.smithy.intellij.psi.SmithyTypes

/**
 * An [Annotator] which provides annotations to [Smithy](https://awslabs.github.io/smithy) model files.
 *
 * All syntax checks which require the higher-level AST (resulting from a complete parse) are handled to complete syntax highlighting from [SmithySyntaxHighlighter].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyAnnotator : Annotator {
    companion object {
        val TOKENS_REQUIRING_TRAILING_NEW_LINE = TokenSet.create(
            SmithyTypes.APPLIED_TRAIT,
            SmithyTypes.CONTROL,
            SmithyTypes.DOCUMENTATION,
            SmithyTypes.IMPORT,
            SmithyTypes.LIST,
            SmithyTypes.MAP,
            SmithyTypes.METADATA,
            SmithyTypes.NAMESPACE,
            SmithyTypes.OPERATION,
            SmithyTypes.RESOURCE,
            SmithyTypes.SERVICE,
            SmithyTypes.SET,
            SmithyTypes.SIMPLE_SHAPE,
            SmithyTypes.STRUCTURE,
            SmithyTypes.TOKEN_LINE_COMMENT,
            SmithyTypes.UNION
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if ((element is SmithyKey || element.elementType == SmithyTypes.TOKEN_DOLLAR_SIGN) && element.parent is SmithyControl) {
            holder.highlight(SmithyColorSettings.CONTROL)
        }
        if (element is SmithyKey && element.parent !is SmithyControl) {
            holder.highlight(SmithyColorSettings.KEY)
        }
        if (element is SmithyMemberName) {
            holder.highlight(SmithyColorSettings.SHAPE_MEMBER)
        }
        if ((element is SmithyShapeId || element.elementType == SmithyTypes.TOKEN_AT) && element.parent is SmithyTrait) {
            holder.highlight(SmithyColorSettings.TRAIT_NAME)
        }
        //Reset all keywords/literals/types used as standalone identifiers back to the normal text color
        if ((element is SmithyBoolean || element is SmithyKeyword || element is SmithyNull || element is SmithySimpleTypeName) && element.parent is SmithyId && element.parent.parent !is SmithyKey) {
            holder.highlight(HighlighterColors.TEXT)
        }
        //Highlight all escape sequences within strings and text blocks
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
        //Since whitespace cannot be referenced within parsing rules, error annotations are added for all nodes which are missing a trailing new-line (or EOF)
        if (element.elementType in TOKENS_REQUIRING_TRAILING_NEW_LINE && element.nextLeaf() != null) {
            val trailingWhiteSpace = element.nextLeafs.takeWhile { it is PsiComment || it is PsiWhiteSpace }
            if (trailingWhiteSpace.none { it.textContains('\n') }) {
                holder.highlight(HighlightSeverity.ERROR, "Expecting trailing line break '\\n'")
            }
        }
        if (element.elementType == SmithyTypes.TOKEN_INCOMPLETE_STRING) {
            holder.highlight(HighlightSeverity.ERROR, "Expecting closing quote '\"'")
        }
        if (element.elementType == SmithyTypes.TOKEN_INCOMPLETE_TEXT_BLOCK) {
            holder.highlight(HighlightSeverity.ERROR, "Expecting closing quotes '\"\"\"'")
        }
        if (element is SmithyIncompleteEntry || element is SmithyIncompleteMember) {
            holder.highlight(HighlightSeverity.ERROR, "Missing shape id")
        }
        if (element is SmithyShapeId && element.parent !is SmithyImport) {
            element.namespaceId?.let { namespaceId ->
                val fix = SmithyOptimizeShapeIdQuickFix(element.project, element)
                holder.newAnnotation(
                    HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING,
                    if (fix.hasImport) "Remove unnecessary qualifier" else "Add import for: ${element.text}"
                ).range(namespaceId.textRange).highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL).withFix(fix)
                    .create()
            }
        }
        if (element is SmithyImport && element in SmithyImportOptimizer.unusedImports(element.containingFile)) {
            holder.newAnnotation(HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING, "Unused import")
                .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL).withFix(SmithyRemoveUnusedImportsQuickFix)
                .create()
        }
        element.reference.let { it as? SmithyShapeReference<*> }?.let {
            if (!it.isSoft && it.resolve() == null) {
                holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved shape: ${element.text}")
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .withFix(SmithyImportShapeQuickFix(element.project, element.text)).create()
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
        severity: HighlightSeverity, message: String, key: TextAttributesKey, range: TextRange
    ) = newAnnotation(severity, message).textAttributes(key).range(range).create()
}