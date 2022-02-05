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

public class SmithyIdImpl extends ASTWrapperPsiElement implements SmithyId {

  public SmithyIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitId(this);
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
  public SmithyKeyword getKeyword() {
    return findChildByClass(SmithyKeyword.class);
  }

  @Override
  @Nullable
  public SmithyNull getNull() {
    return findChildByClass(SmithyNull.class);
  }

  @Override
  @Nullable
  public SmithySimpleTypeName getSimpleTypeName() {
    return findChildByClass(SmithySimpleTypeName.class);
  }

  @Override
  @Nullable
  public SmithySymbol getSymbol() {
    return findChildByClass(SmithySymbol.class);
  }

}