// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.smithy.intellij.psi.SmithyId;
import software.amazon.smithy.intellij.psi.SmithyKeyword;
import software.amazon.smithy.intellij.psi.SmithySymbol;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyIdImpl extends ASTWrapperPsiElement implements SmithyId {

  public SmithyIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyKeyword getKeyword() {
    return findChildByClass(SmithyKeyword.class);
  }

  @Override
  @Nullable
  public SmithySymbol getSymbol() {
    return findChildByClass(SmithySymbol.class);
  }

}
