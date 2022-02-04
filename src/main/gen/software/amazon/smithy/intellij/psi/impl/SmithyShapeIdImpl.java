// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.smithy.intellij.psi.SmithyRootShapeId;
import software.amazon.smithy.intellij.psi.SmithyShapeId;
import software.amazon.smithy.intellij.psi.SmithyShapeIdMember;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyShapeIdImpl extends ASTWrapperPsiElement implements SmithyShapeId {

  public SmithyShapeIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShapeId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SmithyRootShapeId getRootShapeId() {
    return findNotNullChildByClass(SmithyRootShapeId.class);
  }

  @Override
  @Nullable
  public SmithyShapeIdMember getShapeIdMember() {
    return findChildByClass(SmithyShapeIdMember.class);
  }

}
