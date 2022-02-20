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

public class SmithyMapImpl extends SmithyShapeImpl implements SmithyMap {

  public SmithyMapImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitMap(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyDocumentation getDocumentation() {
    return findChildByClass(SmithyDocumentation.class);
  }

  @Override
  @NotNull
  public SmithyShapeBody getBody() {
    return findNotNullChildByClass(SmithyShapeBody.class);
  }

}
