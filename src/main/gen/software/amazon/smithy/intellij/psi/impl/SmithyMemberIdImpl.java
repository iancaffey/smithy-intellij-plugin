// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import software.amazon.smithy.intellij.ext.SmithyPsiElement;
import software.amazon.smithy.intellij.psi.*;
import software.amazon.smithy.intellij.ext.SmithyPsiImplUtilKt;
import software.amazon.smithy.intellij.SmithyMemberReference;

public class SmithyMemberIdImpl extends SmithyPsiElement implements SmithyMemberId {

  public SmithyMemberIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitMemberId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SmithyMemberName getMemberName() {
    return findNotNullChildByClass(SmithyMemberName.class);
  }

  @Override
  @NotNull
  public SmithyShapeId getShapeId() {
    return findNotNullChildByClass(SmithyShapeId.class);
  }

  @Override
  @NotNull
  public String getId() {
    return SmithyPsiImplUtilKt.getId(this);
  }

  @Override
  @NotNull
  public SmithyMemberReference getReference() {
    return SmithyPsiImplUtilKt.getReference(this);
  }

  @Override
  @NotNull
  public String toString() {
    return SmithyPsiImplUtilKt.toString(this);
  }

}
