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
import com.intellij.psi.tree.IElementType;

public class SmithyDocumentationImpl extends SmithyPsiElement implements SmithyDocumentation {

  public SmithyDocumentationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitDocumentation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public PsiElement getOwner() {
    return SmithyPsiImplUtil.getOwner(this);
  }

  @Override
  public @NotNull IElementType getTokenType() {
    return SmithyPsiImplUtil.getTokenType(this);
  }

  @Override
  public @NotNull String toDocString() {
    return SmithyPsiImplUtil.toDocString(this);
  }

}
