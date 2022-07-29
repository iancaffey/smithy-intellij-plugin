package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiNamedElement

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) target definition (either in an AST or IDL) in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyAstTarget
 * @see SmithyShapeId
 * @see SmithySyntheticShapeTarget
 */
interface SmithyShapeTarget : SmithyElement, PsiNamedElement {
    override fun getName(): String
    val shapeName: String
    val declaredNamespace: String?
    val resolvedNamespace: String?
    fun resolve(): SmithyShapeDefinition?
}