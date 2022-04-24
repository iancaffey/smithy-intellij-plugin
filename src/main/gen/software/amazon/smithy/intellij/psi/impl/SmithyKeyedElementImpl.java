// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import software.amazon.smithy.intellij.ext.SmithyPsiElement;
import software.amazon.smithy.intellij.psi.*;
import software.amazon.smithy.intellij.ext.SmithyPsiImplUtilKt;

public class SmithyKeyedElementImpl extends SmithyPsiElement implements SmithyKeyedElement {

  public SmithyKeyedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitKeyedElement(this);
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
  public String getName() {
    return SmithyPsiImplUtilKt.getName(this);
  }

  @Override
  @NotNull
  public SmithyKeyedElement setName(@Nullable String newName) {
    return SmithyPsiImplUtilKt.setName(this, newName);
  }

  @Override
  @NotNull
  public SmithyKey getNameIdentifier() {
    return SmithyPsiImplUtilKt.getNameIdentifier(this);
  }

  @Override
  public int getTextOffset() {
    return SmithyPsiImplUtilKt.getTextOffset(this);
  }

}
