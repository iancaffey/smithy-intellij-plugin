package software.amazon.smithy.intellij.psi

import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.tree.IElementType

/**
 * A [documentation](https://awslabs.github.io/smithy/1.0/spec/core/documentation-traits.html#documentation-trait) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstDocumentation(
    val target: SmithyDefinition, val markdown: String
) : FakePsiElement(), SmithyDocumentationDefinition {
    override fun getOwner() = target
    override fun getParent() = target
    override fun getTokenType(): IElementType = SmithyTypes.DOCUMENTATION
    override fun toDocString() = markdown
}
