package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyElement;

/**
 * A base {@link PsiElement} for all {@link SmithyElement} implementations.
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class SmithyPsiElement extends ASTWrapperPsiElement {
    public SmithyPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        String name = getName();
        return name != null ? name : getText();
    }
}
