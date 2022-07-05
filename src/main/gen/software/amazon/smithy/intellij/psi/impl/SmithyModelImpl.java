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

public class SmithyModelImpl extends SmithyPsiElement implements SmithyModel {

  public SmithyModelImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitModel(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public String getNamespace() {
    return SmithyPsiImplUtilKt.getNamespace(this);
  }

  @Override
  @NotNull
  public List<SmithyShape> getShapes() {
    return SmithyPsiImplUtilKt.getShapes(this);
  }

}
