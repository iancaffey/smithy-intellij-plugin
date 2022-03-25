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
    override fun getQualifiedName(item: NavigationItem) = (item as SmithyShape).let { "${it.namespace}#${it.name}" }
    override fun getQualifiedNameSeparator() = "#"
    override fun getElementKind() = "shape"
    override fun getElementKindsPluralized() = listOf("Shapes")
    override fun getTabTitlePluralized() = "Shapes"
    override fun getElementLanguage() = SmithyLanguage
    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        findShapes(scope).forEach { processor.process(it.name) }
    }

    override fun processElementsWithName(
        name: String, processor: Processor<in NavigationItem>, parameters: FindSymbolParameters
    ) {
        findShapes(parameters.searchScope).forEach { if (it.name == name) processor.process(it) }
    }

    private fun findShapes(scope: GlobalSearchScope) = FileTypeIndex.getFiles(SmithyFileType, scope).flatMap {
        val file = PsiManager.getInstance(scope.project!!).findFile(it) as? SmithyFile
        file?.model?.shapes ?: emptyList()
    }
}