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
import software.amazon.smithy.intellij.ext.SmithyPsiImplUtilKt;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SmithyNumberImpl extends SmithyPrimitiveImpl implements SmithyNumber {

  public SmithyNumberImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitNumber(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public double byteValue() {
    return SmithyPsiImplUtilKt.byteValue(this);
  }

  @Override
  public double shortValue() {
    return SmithyPsiImplUtilKt.shortValue(this);
  }

  @Override
  public double intValue() {
    return SmithyPsiImplUtilKt.intValue(this);
  }

  @Override
  public long longValue() {
    return SmithyPsiImplUtilKt.longValue(this);
  }

  @Override
  public float floatValue() {
    return SmithyPsiImplUtilKt.floatValue(this);
  }

  @Override
  public double doubleValue() {
    return SmithyPsiImplUtilKt.doubleValue(this);
  }

  @Override
  @NotNull
  public BigDecimal bigDecimalValue() {
    return SmithyPsiImplUtilKt.bigDecimalValue(this);
  }

  @Override
  @NotNull
  public BigInteger bigIntegerValue() {
    return SmithyPsiImplUtilKt.bigIntegerValue(this);
  }

}
