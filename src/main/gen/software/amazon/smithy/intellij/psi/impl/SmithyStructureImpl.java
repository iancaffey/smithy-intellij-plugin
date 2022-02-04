// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyEntry;
import software.amazon.smithy.intellij.psi.SmithyStructure;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

import java.util.List;

public class SmithyStructureImpl extends ASTWrapperPsiElement implements SmithyStructure {

  public SmithyStructureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitStructure(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SmithyEntry> getEntryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyEntry.class);
  }

}
