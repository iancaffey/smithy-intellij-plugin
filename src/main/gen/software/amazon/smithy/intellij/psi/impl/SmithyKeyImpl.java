// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.smithy.intellij.psi.SmithyId;
import software.amazon.smithy.intellij.psi.SmithyKey;
import software.amazon.smithy.intellij.psi.SmithyString;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyKeyImpl extends ASTWrapperPsiElement implements SmithyKey {

  public SmithyKeyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitKey(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyId getId() {
    return findChildByClass(SmithyId.class);
  }

  @Override
  @Nullable
  public SmithyString getString() {
    return findChildByClass(SmithyString.class);
  }

}
