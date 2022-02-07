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
import java.math.BigDecimal;
import java.math.BigInteger;

public class SmithyNumberImpl extends ASTWrapperPsiElement implements SmithyNumber {

  public SmithyNumberImpl(@NotNull ASTNode node) {
    super(node);
  }

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
    return SmithyPsiImplUtil.byteValue(this);
  }

  @Override
  public double shortValue() {
    return SmithyPsiImplUtil.shortValue(this);
  }

  @Override
  public double intValue() {
    return SmithyPsiImplUtil.intValue(this);
  }

  @Override
  public long longValue() {
    return SmithyPsiImplUtil.longValue(this);
  }

  @Override
  public float floatValue() {
    return SmithyPsiImplUtil.floatValue(this);
  }

  @Override
  public double doubleValue() {
    return SmithyPsiImplUtil.doubleValue(this);
  }

  @Override
  public BigDecimal bigDecimalValue() {
    return SmithyPsiImplUtil.bigDecimalValue(this);
  }

  @Override
  public BigInteger bigIntegerValue() {
    return SmithyPsiImplUtil.bigIntegerValue(this);
  }

}
