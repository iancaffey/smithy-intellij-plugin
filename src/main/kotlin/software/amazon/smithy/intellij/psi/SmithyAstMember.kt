package software.amazon.smithy.intellij.psi

import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import software.amazon.smithy.intellij.SmithyAst
import software.amazon.smithy.intellij.SmithyIcons
import software.amazon.smithy.intellij.SmithyShapeResolver

/**
 * A [FakePsiElement] which represents a member from [SmithyAst].
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAstMember(
    override val enclosingShape: SmithyAstShape, val memberName: String, val reference: SmithyAst.Reference
) : FakePsiElement(), SmithyMemberDefinition {
    companion object {
        private val dependencies = listOf(PsiModificationTracker.MODIFICATION_COUNT)
        private fun resolver(member: SmithyAstMember) = CachedValueProvider {
            val results = SmithyShapeResolver.resolve(member)
            CachedValueProvider.Result.create(if (results.size == 1) results.first() else null, dependencies)
        }
    }

    override val targetShapeId = reference.target
    override fun getName() = memberName
    override fun getParent(): SmithyAstShape = enclosingShape
    override fun getLocationString(): String = enclosingShape.locationString
    override fun getIcon(unused: Boolean) = SmithyIcons.MEMBER
    override fun resolve(): SmithyShapeDefinition? = CachedValuesManager.getCachedValue(this, resolver(this))
    override fun toString() = "$parent$$memberName"
}