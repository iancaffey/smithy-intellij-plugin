package software.amazon.smithy.intellij

import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.GotoClassContributor
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import software.amazon.smithy.intellij.psi.SmithyShape

/**
 * A [GotoClassContributor] which finds all [SmithyShape] within the project to display in searches.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyGotoShapeContributor : GotoClassContributor, ChooseByNameContributorEx {
    override fun getQualifiedName(item: NavigationItem) = when (item) {
        is SmithyShape -> "${item.namespace}#${item.name}"
        else -> item.name
    }

    override fun getQualifiedNameSeparator() = "#"
    override fun getElementKind() = "shape"
    override fun getElementKindsPluralized() = listOf("Shapes")
    override fun getTabTitlePluralized() = "Shapes"
    override fun getElementLanguage() = SmithyLanguage
    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        processShapes(scope) { processor.process(it.name) }
    }

    override fun processElementsWithName(
        name: String, processor: Processor<in NavigationItem>, parameters: FindSymbolParameters
    ) {
        processShapes(parameters.searchScope) { if (it.name == name) processor.process(it) }
    }

    private fun processShapes(scope: GlobalSearchScope, action: (SmithyShape) -> Unit) =
        FileTypeIndex.getFiles(SmithyFileType, scope).forEach {
            val file = PsiManager.getInstance(scope.project!!).findFile(it) as? SmithyFile ?: return@forEach
            file.model.shapes.forEach(action)
        }
}