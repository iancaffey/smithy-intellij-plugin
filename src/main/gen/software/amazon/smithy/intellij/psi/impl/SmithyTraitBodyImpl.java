// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import software.amazon.smithy.intellij.psi.*;

public class SmithyTraitBodyImpl extends SmithyPsiElement implements SmithyTraitBody {

  public SmithyTraitBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitTraitBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyValue getValue() {
    return findChildByClass(SmithyValue.class);
  }

  @Override
  @NotNull
  public List<SmithyEntry> getValues() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyEntry.class);
  }

}
