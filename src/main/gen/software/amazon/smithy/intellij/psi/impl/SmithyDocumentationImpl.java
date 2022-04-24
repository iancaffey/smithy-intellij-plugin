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
  @NotNull
  public PsiElement getOwner() {
    return SmithyPsiImplUtilKt.getOwner(this);
  }

  @Override
  @NotNull
  public IElementType getTokenType() {
    return SmithyPsiImplUtilKt.getTokenType(this);
  }

  @Override
  @NotNull
  public String toDocString() {
    return SmithyPsiImplUtilKt.toDocString(this);
  }

}
