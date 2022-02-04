// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyId;
import software.amazon.smithy.intellij.psi.SmithySimpleShapeDefinition;
import software.amazon.smithy.intellij.psi.SmithySimpleTypeName;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithySimpleShapeDefinitionImpl extends ASTWrapperPsiElement implements SmithySimpleShapeDefinition {

  public SmithySimpleShapeDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitSimpleShapeDefinition(this);
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
  public SmithySimpleTypeName getSimpleTypeName() {
    return findNotNullChildByClass(SmithySimpleTypeName.class);
  }

}
