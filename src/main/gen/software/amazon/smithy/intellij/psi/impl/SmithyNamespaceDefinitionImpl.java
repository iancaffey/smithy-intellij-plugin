// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyNamespace;
import software.amazon.smithy.intellij.psi.SmithyNamespaceDefinition;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyNamespaceDefinitionImpl extends ASTWrapperPsiElement implements SmithyNamespaceDefinition {

  public SmithyNamespaceDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitNamespaceDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SmithyNamespace getNamespace() {
    return findNotNullChildByClass(SmithyNamespace.class);
  }

}
