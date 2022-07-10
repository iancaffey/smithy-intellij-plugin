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
    fun hasTrait(shapeId: String) = findTrait(shapeId) != null
    fun findTrait(shapeId: String): SmithyTraitDefinition?
    val declaredTraits: List<@JvmWildcard SmithyTraitDefinition>
    val documentation: SmithyDocumentationDefinition?
    //TODO: add consolidated node value(s) API that unpacks any literals into the corresponding value
}