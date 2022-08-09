package software.amazon.smithy.intellij

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import software.amazon.smithy.intellij.psi.SmithyResourcePropertyDefinition

/**
 * A [ChooseByNameContributor] which finds all [SmithyResourcePropertyDefinition] within the project to display in searches.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyGotoPropertyContributor : ChooseByNameContributorEx {
    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        processProperties(scope) { processor.process(it.name) }
    }

    override fun processElementsWithName(
        name: String, processor: Processor<in NavigationItem>, parameters: FindSymbolParameters
    ) {
        processProperties(parameters.searchScope) { if (it.name == name) processor.process(it) }
    }

    private fun processProperties(scope: GlobalSearchScope, action: (SmithyResourcePropertyDefinition) -> Unit) {
        SmithyFileIndex.forEach(scope) { processFile(it, action) }
    }

    private fun processFile(file: SmithyFile, action: (SmithyResourcePropertyDefinition) -> Unit) =
        file.model?.shapes?.forEach { shape -> shape.properties.forEach(action) }
}