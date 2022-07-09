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
sealed interface SmithyDefinition : NavigatablePsiElement, PsiNamedElement {
    override fun getName(): String
}