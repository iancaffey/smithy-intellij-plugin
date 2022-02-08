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

public abstract class SmithyShapeImpl extends ASTWrapperPsiElement implements SmithyShape {

  public SmithyShapeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShape(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public @NotNull String getNamespace() {
    return SmithyPsiImplUtil.getNamespace(this);
  }

  @Override
  public @NotNull String getName() {
    return SmithyPsiImplUtil.getName(this);
  }

  @Override
  public @Nullable SmithyDocumentation getDocumentation() {
    return SmithyPsiImplUtil.getDocumentation(this);
  }

  @Override
  public @NotNull List<SmithyTrait> getDeclaredTraits() {
    return SmithyPsiImplUtil.getDeclaredTraits(this);
  }

}
