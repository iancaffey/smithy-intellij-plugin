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
  public SmithyNamespace getNamespace() {
    return findNotNullChildByClass(SmithyNamespace.class);
  }

  @Override
  @NotNull
  public List<SmithyImport> getImports() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyImport.class);
  }

  @Override
  @NotNull
  public List<SmithyShapeStatement> getStatements() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyShapeStatement.class);
  }

}
