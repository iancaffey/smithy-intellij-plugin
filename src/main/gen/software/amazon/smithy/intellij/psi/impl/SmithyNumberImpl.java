// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyNumber;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

import static software.amazon.smithy.intellij.psi.SmithyTypes.TOKEN_NUMBER;

public class SmithyNumberImpl extends ASTWrapperPsiElement implements SmithyNumber {

  public SmithyNumberImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitNumber(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getTokenNumber() {
    return findNotNullChildByType(TOKEN_NUMBER);
  }

}
