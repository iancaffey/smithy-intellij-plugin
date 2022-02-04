// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.smithy.intellij.psi.SmithyApply;
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition;
import software.amazon.smithy.intellij.psi.SmithyShapeStatement;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyShapeStatementImpl extends ASTWrapperPsiElement implements SmithyShapeStatement {

  public SmithyShapeStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShapeStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyApply getApply() {
    return findChildByClass(SmithyApply.class);
  }

  @Override
  @Nullable
  public SmithyShapeDefinition getShapeDefinition() {
    return findChildByClass(SmithyShapeDefinition.class);
  }

}
