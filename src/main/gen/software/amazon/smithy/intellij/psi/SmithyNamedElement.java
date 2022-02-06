package software.amazon.smithy.intellij.psi;

import com.intellij.psi.PsiElement;

/**
 * A {@link PsiElement} within a {@link SmithyModel} which can be referred to by name.
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface SmithyNamedElement extends SmithyElement {
    String getName();
}
