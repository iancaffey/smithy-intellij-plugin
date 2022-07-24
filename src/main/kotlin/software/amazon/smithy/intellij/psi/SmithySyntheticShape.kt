package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.FakePsiElement
import software.amazon.smithy.intellij.SmithyLanguage

/**
 * A synthetic shape which refines `smithy.api#Unit` to a specific type.
 *
 * [SmithyEnumMember] and [SmithyIntEnumMember] target `smithy.api#Unit` but have values with types of `string` and `integer` respectively.
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithySyntheticShape(
    private val enclosing: PsiElement,
    override val type: String
) : FakePsiElement(), SmithyShapeDefinition {
    private val id = let {
        object : FakePsiElement() {
            override fun getName() = type
            override fun getParent() = it
        }
    }
    override val namespace = "smithy.api"
    override val shapeName = "Unit"
    override val shapeId = "smithy.api#Unit"
    override val members = emptyList<SmithyMemberDefinition>()
    override val declaredTraits = emptyList<SmithyTraitDefinition>()
    override val documentation: SmithyDocumentationDefinition? = null
    override fun getName() = type
    override fun getNameIdentifier() = id
    override fun getLanguage() = SmithyLanguage
    override fun getParent() = enclosing
}
