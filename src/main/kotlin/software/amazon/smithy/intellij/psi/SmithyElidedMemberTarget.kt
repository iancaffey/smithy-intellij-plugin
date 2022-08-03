package software.amazon.smithy.intellij.psi

/**
 * A target of a [SmithyElidedMember].
 *
 * @author Ian Caffey
 * @since 1.0
 * @see SmithyMemberDefinition
 * @see SmithyResourceIdentifierDefinition
 */
interface SmithyElidedMemberTarget : SmithyDefinition {
    val enclosingShape: SmithyShapeDefinition
    val declaredTarget: SmithyShapeTarget?
    val resolvedTarget: SmithyShapeTarget? get() = declaredTarget
    fun resolve() = resolvedTarget?.resolve()
}