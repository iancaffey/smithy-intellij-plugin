// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import software.amazon.smithy.intellij.psi.*;

public class SmithyNamespaceIdImpl extends SmithyPsiElement implements SmithyNamespaceId {

  public SmithyNamespaceIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitNamespaceId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public @NotNull String toString() {
    return SmithyPsiImplUtil.toString(this);
  }

  @Override
  @NotNull
  public List<SmithyId> getParts() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyId.class);
  }

}