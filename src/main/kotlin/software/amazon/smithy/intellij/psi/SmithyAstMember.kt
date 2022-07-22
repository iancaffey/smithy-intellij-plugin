package software.amazon.smithy.intellij.psi

import com.intellij.psi.impl.FakePsiElement
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons
import software.amazon.smithy.intellij.SmithyShapeResolver

/**
 * A [shape member](https://awslabs.github.io/smithy/1.0/spec/core/model.html#member) definition in the [Smithy](https://awslabs.github.io/smithy) [AST](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstMember(
    override val enclosingShape: SmithyAstShape, val memberName: String, val reference: SmithyAst.Reference
) : FakePsiElement(), SmithyAstDefinition, SmithyMemberDefinition {
    private val targetParts = reference.target.split('#', limit = 2)
    override val targetShapeName = targetParts[1]
    override val declaredTargetNamespace = targetParts[0]
    override val resolvedTargetNamespace = targetParts[0]
    override val documentation = reference.traits?.get("smithy.api#documentation")?.asString()?.let {
        SmithyAstDocumentation(this, it)
    }
    override val declaredTraits = (reference.traits ?: emptyMap()).entries.map { (key, value) ->
        SmithyAstTrait(this, key, value)
    }

    override fun getName() = memberName
    override fun getParent(): SmithyAstShape = enclosingShape
    override fun getLocationString(): String = enclosingShape.locationString
    override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
    override fun resolve() = SmithyShapeResolver.getDefinitions(this).firstOrNull()
    override fun toString() = "$parent$$memberName"
}