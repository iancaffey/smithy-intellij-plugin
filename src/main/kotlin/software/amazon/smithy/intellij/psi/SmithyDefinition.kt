package software.amazon.smithy.intellij.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNamedElement

/**
 * A definition in [Smithy](https://awslabs.github.io/smithy) (either within an AST or IDL).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyMemberDefinition
 * @see SmithyShapeDefinition
 */
sealed interface SmithyDefinition : SmithyElement, NavigatablePsiElement, PsiNamedElement {
    override fun getName(): String
    val appliedTraits: List<@JvmWildcard SmithyTraitDefinition>
    val declaredTraits: List<@JvmWildcard SmithyTraitDefinition>
    val documentation: SmithyDocumentationDefinition?
    fun hasTrait(namespace: String, shapeName: String) = findTrait(namespace, shapeName) != null
    fun findTrait(namespace: String, shapeName: String) = declaredTraits.find {
        it.shapeName == shapeName && it.resolvedNamespace == namespace
    } ?: appliedTraits.find { it.shapeName == shapeName && it.resolvedNamespace == namespace }
}