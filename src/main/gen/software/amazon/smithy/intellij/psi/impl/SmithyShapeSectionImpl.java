// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyImports;
import software.amazon.smithy.intellij.psi.SmithyNamespaceDefinition;
import software.amazon.smithy.intellij.psi.SmithyShapeSection;
import software.amazon.smithy.intellij.psi.SmithyShapeStatements;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyShapeSectionImpl extends ASTWrapperPsiElement implements SmithyShapeSection {

  public SmithyShapeSectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShapeSection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SmithyImports getImports() {
    return findNotNullChildByClass(SmithyImports.class);
  }

  @Override
  @NotNull
  public SmithyNamespaceDefinition getNamespaceDefinition() {
    return findNotNullChildByClass(SmithyNamespaceDefinition.class);
  }

  @Override
  @NotNull
  public SmithyShapeStatements getShapeStatements() {
    return findNotNullChildByClass(SmithyShapeStatements.class);
  }

}
