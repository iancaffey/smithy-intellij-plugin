// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyControlDefinition;
import software.amazon.smithy.intellij.psi.SmithyControlSection;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

import java.util.List;

public class SmithyControlSectionImpl extends ASTWrapperPsiElement implements SmithyControlSection {

  public SmithyControlSectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitControlSection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SmithyControlDefinition> getControlDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyControlDefinition.class);
  }

}
