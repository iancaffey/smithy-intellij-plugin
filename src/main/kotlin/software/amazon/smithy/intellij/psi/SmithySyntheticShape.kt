package software.amazon.smithy.intellij.psi

import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.SmithyIcons

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
) : SmithySyntheticElement(), SmithyShapeDefinition {
    private val id = let {
        object : SmithySyntheticElement() {
            override fun getName() = type
            override fun getParent() = it
        }
    }
    override val namespace = "smithy.api"
    override val shapeName = "Unit"
    override val shapeId = "smithy.api#Unit"
    override val mixins = emptyList<SmithyShapeTarget>()
    override val declaredMembers = emptyList<SmithyMemberDefinition>()
    override val declaredTraits = emptyList<SmithyTraitDefinition>()
    override fun getName() = type
    override fun getNameIdentifier() = id
    override fun getParent() = enclosing
    override fun getLocationString() = namespace
    override fun getIcon(unused: Boolean) = SmithyIcons.SHAPE
    override fun toString() = shapeId
}

data class SmithySyntheticShapeTarget(
    val member: SmithyMemberDefinition,
    val type: String
) : SmithySyntheticElement(), SmithyShapeTarget {
    val shape = SmithySyntheticShape(member, type)
    override val shapeName = shape.name
    override val declaredNamespace = shape.namespace
    override val resolvedNamespace = shape.namespace
    override fun getName() = shapeName
    override fun getParent() = member
    override fun resolve() = shape
}
