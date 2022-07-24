package software.amazon.smithy.intellij

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition

/**
 * A [ChooseByNameContributor] which finds all [SmithyMemberDefinition] within the project to display in searches.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyGotoMemberContributor : ChooseByNameContributorEx {
    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        processMembers(scope) { processor.process(it.name) }
    }

    override fun processElementsWithName(
        name: String, processor: Processor<in NavigationItem>, parameters: FindSymbolParameters
    ) {
        processMembers(parameters.searchScope) { if (it.name == name) processor.process(it) }
    }

    private fun processMembers(scope: GlobalSearchScope, action: (SmithyMemberDefinition) -> Unit) {
        SmithyFileIndex.forEach(scope) { processFile(it, action) }
    }

    private fun processFile(file: SmithyFile, action: (SmithyMemberDefinition) -> Unit) =
        file.model?.shapes?.forEach { shape -> shape.members.forEach(action) }
}