package software.amazon.smithy.intellij.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNamedElement
import software.amazon.smithy.intellij.SmithyShapeResolver

/**
 * A [trait](https://awslabs.github.io/smithy/1.0/spec/core/model.html#traits) definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyTrait
 * @see SmithyAstTrait
 */
interface SmithyTraitDefinition : SmithyElement, NavigatablePsiElement, PsiNamedElement {
    val shapeName: String
    val declaredNamespace: String?
    val resolvedNamespace: String?
    val value: SmithyValueDefinition
    fun resolve() = SmithyShapeResolver.getDefinitions(this).firstOrNull()
    fun toDocString(): String
}