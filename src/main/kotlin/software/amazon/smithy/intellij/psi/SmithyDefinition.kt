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
    val traits: List<SmithyTraitDefinition> get() = listOf(appliedTraits, declaredTraits, syntheticTraits).flatten()
    val appliedTraits: List<@JvmWildcard SmithyTraitDefinition>
    val declaredTraits: List<@JvmWildcard SmithyTraitDefinition>
    val syntheticTraits: List<@JvmWildcard SmithyTraitDefinition> get() = emptyList()
    val documentation: SmithyDocumentationDefinition?
    fun hasTrait(namespace: String, shapeName: String) = findTrait(namespace, shapeName) != null
    fun findTrait(namespace: String, shapeName: String) =
        findTraitIn(syntheticTraits, namespace, shapeName)
            ?: findTraitIn(declaredTraits, namespace, shapeName)
            ?: findTraitIn(appliedTraits, namespace, shapeName)
}

fun hasTraitIn(traits: List<SmithyTraitDefinition>, namespace: String, shapeName: String) = traits.any {
    it.shapeName == shapeName && it.resolvedNamespace == namespace
}

fun findTraitIn(traits: List<SmithyTraitDefinition>, namespace: String, shapeName: String) = traits.find {
    it.shapeName == shapeName && it.resolvedNamespace == namespace
}