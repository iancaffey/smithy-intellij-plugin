// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyControlDefinition;
import software.amazon.smithy.intellij.psi.SmithyKey;
import software.amazon.smithy.intellij.psi.SmithyValue;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyControlDefinitionImpl extends ASTWrapperPsiElement implements SmithyControlDefinition {

  public SmithyControlDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitControlDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SmithyKey getKey() {
    return findNotNullChildByClass(SmithyKey.class);
  }

  @Override
  @NotNull
  public SmithyValue getValue() {
    return findNotNullChildByClass(SmithyValue.class);
  }

}
