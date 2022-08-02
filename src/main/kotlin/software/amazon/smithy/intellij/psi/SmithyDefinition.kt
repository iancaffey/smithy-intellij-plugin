package software.amazon.smithy.intellij.psi

import com.intellij.psi.NavigatablePsiElement

/**
 * A definition in [Smithy](https://awslabs.github.io/smithy) (either within an AST or IDL).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyMemberDefinition
 * @see SmithyShapeDefinition
 */
sealed interface SmithyDefinition : SmithyNamedElement, NavigatablePsiElement {
    val href: String
    val appliedTraits: List<@JvmWildcard SmithyTraitDefinition>
    val declaredTraits: List<@JvmWildcard SmithyTraitDefinition>
    val syntheticTraits: List<@JvmWildcard SmithyTraitDefinition> get() = emptyList()
    val traits: List<SmithyTraitDefinition>
    val documentation: SmithyDocumentationDefinition?
    fun hasTrait(namespace: String, shapeName: String) = hasTraitIn(traits, namespace, shapeName)
    fun findTrait(namespace: String, shapeName: String) = traits.find {
        it.shapeName == shapeName && it.resolvedNamespace == namespace
    }
}

fun hasTraitIn(traits: List<SmithyTraitDefinition>, namespace: String, shapeName: String) = traits.any {
    it.shapeName == shapeName && it.resolvedNamespace == namespace
}