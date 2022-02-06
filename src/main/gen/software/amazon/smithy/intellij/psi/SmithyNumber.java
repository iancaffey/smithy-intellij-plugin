// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface SmithyNumber extends SmithyElement {

  @NotNull
  PsiElement getTokenNumber();

  double byteValue();

  double shortValue();

  double intValue();

  long longValue();

  float floatValue();

  double doubleValue();

  BigDecimal bigDecimalValue();

  BigInteger bigIntegerValue();

}
