// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.smithy.intellij.psi.SmithyBoolean;
import software.amazon.smithy.intellij.psi.SmithyNull;
import software.amazon.smithy.intellij.psi.SmithyNumber;
import software.amazon.smithy.intellij.psi.SmithyPrimitive;
import software.amazon.smithy.intellij.psi.SmithyShapeId;
import software.amazon.smithy.intellij.psi.SmithyString;
import software.amazon.smithy.intellij.psi.SmithyTextBlock;
import software.amazon.smithy.intellij.psi.SmithyVisitor;

public class SmithyPrimitiveImpl extends ASTWrapperPsiElement implements SmithyPrimitive {

  public SmithyPrimitiveImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitPrimitive(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyBoolean getBoolean() {
    return findChildByClass(SmithyBoolean.class);
  }

  @Override
  @Nullable
  public SmithyNull getNull() {
    return findChildByClass(SmithyNull.class);
  }

  @Override
  @Nullable
  public SmithyNumber getNumber() {
    return findChildByClass(SmithyNumber.class);
  }

  @Override
  @Nullable
  public SmithyShapeId getShapeId() {
    return findChildByClass(SmithyShapeId.class);
  }

  @Override
  @Nullable
  public SmithyString getString() {
    return findChildByClass(SmithyString.class);
  }

  @Override
  @Nullable
  public SmithyTextBlock getTextBlock() {
    return findChildByClass(SmithyTextBlock.class);
  }

}
