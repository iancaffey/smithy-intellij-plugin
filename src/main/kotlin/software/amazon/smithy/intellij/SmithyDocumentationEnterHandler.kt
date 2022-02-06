package software.amazon.smithy.intellij

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate.Result
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.psi.SmithyDocumentation

/**
 * An [EnterHandlerDelegate] which handles appending [///] when enter is pressed within [SmithyDocumentation].
 *
 * Leading whitespace from the previous documentation comment line will be preserved for the newly created line.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyDocumentationEnterHandler : EnterHandlerDelegateAdapter(), EnterHandlerDelegate {
    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): Result {
        val offset = caretOffset.get()
        //Note: existing doc comment will be identified 1 behind the current offset to support appending more doc lines
        val doc = PsiTreeUtil.findElementOfClassAtOffset(
            file, offset - 1, SmithyDocumentation::class.java, false
        ) ?: return Result.Continue
        //Note: this will keep the same indent as the previous doc line before the caret (even if it's only whitespace)
        var indent = 0
        val docUntilCaret = doc.text.substring(0, offset - doc.textRange.startOffset)
        val lastDocLineIndex = docUntilCaret.lastIndexOf("///")
        if (lastDocLineIndex != -1) {
            val lastDocLineText = docUntilCaret.substring(lastDocLineIndex + 3)
            val firstCharOffset = lastDocLineText.indexOfFirst { !it.isWhitespace() }
            indent = if (firstCharOffset == -1) lastDocLineText.length else firstCharOffset
        }
        val prefix = "///${" ".repeat(indent)}"
        editor.document.insertString(offset, prefix)
        caretAdvance.set(prefix.length)
        return Result.DefaultForceIndent
    }
}
