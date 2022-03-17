package software.amazon.smithy.intellij

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiFile
import software.amazon.smithy.intellij.psi.SmithyAggregateShape
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyShape

/**
 * A [PsiStructureViewFactory] for [Smithy](https://awslabs.github.io/smithy) IDL model files.
 *
 * [SmithyStructureViewFactory] provides a structure view of each [SmithyShape] within a [SmithyFile], as well as each [SmithyMember] within [SmithyAggregateShape].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(file: PsiFile): StructureViewBuilder =
        object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?) = SmithyStructureViewModel(file)
        }
}

/**
 * A [StructureViewModel] for [SmithyFile].
 *
 * [SmithyFile] will have a root [SmithyStructureViewElement] which begins the structure view by enumerating all shapes within [SmithyFile.model].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyStructureViewModel(
    psiFile: PsiFile
) : StructureViewModelBase(psiFile, SmithyStructureViewElement(psiFile)), ElementInfoProvider {
    override fun getSorters() = arrayOf(Sorter.ALPHA_SORTER)
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement) = false
    override fun isAlwaysLeaf(element: StructureViewTreeElement) = false
}

/**
 * A [StructureViewTreeElement] for any [NavigatablePsiElement] within a [SmithyFile].
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyStructureViewElement(val element: NavigatablePsiElement) : StructureViewTreeElement, SortableTreeElement {
    override fun getValue() = element
    override fun canNavigate() = element.canNavigate()
    override fun canNavigateToSource() = element.canNavigateToSource()
    override fun getAlphaSortKey() = element.name ?: ""
    override fun getPresentation() = element.presentation ?: PresentationData()
    override fun getChildren(): Array<out TreeElement> = when (element) {
        is SmithyFile -> element.model.shapes.map { SmithyStructureViewElement(it) }.toTypedArray()
        is SmithyAggregateShape -> element.body.members.map { SmithyStructureViewElement(it) }.toTypedArray()
        else -> StructureViewTreeElement.EMPTY_ARRAY
    }

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }
}
