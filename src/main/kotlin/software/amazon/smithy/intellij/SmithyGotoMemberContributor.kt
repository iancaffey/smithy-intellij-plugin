package software.amazon.smithy.intellij

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import software.amazon.smithy.intellij.psi.SmithyAggregateShape
import software.amazon.smithy.intellij.psi.SmithyMember

/**
 * A [ChooseByNameContributor] which finds all [SmithyMember] within the project to display in searches.
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

    private fun processMembers(scope: GlobalSearchScope, action: (SmithyMember) -> Unit) {
        processFile(SmithyPreludeIndex.getPrelude(scope.project!!), action)
        SmithyFileIndex.forEach(scope) { processFile(it, action) }
    }

    private fun processFile(file: SmithyFile, action: (SmithyMember) -> Unit) = file.model?.shapes?.forEach { shape ->
        if (shape is SmithyAggregateShape) {
            shape.body.members.forEach(action)
        }
    }
}