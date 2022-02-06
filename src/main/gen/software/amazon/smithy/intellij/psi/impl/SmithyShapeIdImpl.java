// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import software.amazon.smithy.intellij.psi.*;

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
  @Nullable
  public SmithyMemberName getMemberName() {
    return findChildByClass(SmithyMemberName.class);
  }

  @Override
  @Nullable
  public SmithyNamespace getNamespace() {
    return findChildByClass(SmithyNamespace.class);
  }

  @Override
  @NotNull
  public SmithyShapeName getShapeName() {
    return findNotNullChildByClass(SmithyShapeName.class);
  }

}
