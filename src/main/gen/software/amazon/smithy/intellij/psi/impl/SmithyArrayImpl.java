// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import software.amazon.smithy.intellij.psi.SmithyArray;
import software.amazon.smithy.intellij.psi.SmithyValue;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

import java.util.List;

public class SmithyArrayImpl extends ASTWrapperPsiElement implements SmithyArray {

  public SmithyArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitArray(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SmithyValue> getValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyValue.class);
  }

}
