// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyId;
import software.amazon.smithy.intellij.psi.SmithyShapeField;
import software.amazon.smithy.intellij.psi.SmithyShapeId;
import software.amazon.smithy.intellij.psi.SmithyTraits;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyShapeFieldImpl extends ASTWrapperPsiElement implements SmithyShapeField {

  public SmithyShapeFieldImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShapeField(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SmithyId getId() {
    return findNotNullChildByClass(SmithyId.class);
  }

  @Override
  @NotNull
  public SmithyShapeId getShapeId() {
    return findNotNullChildByClass(SmithyShapeId.class);
  }

  @Override
  @NotNull
  public SmithyTraits getTraits() {
    return findNotNullChildByClass(SmithyTraits.class);
  }

}
