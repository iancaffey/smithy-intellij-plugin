// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyPrimitive extends PsiElement {

  @Nullable
  SmithyBoolean getBoolean();

  @Nullable
  SmithyNull getNull();

  @Nullable
  SmithyNumber getNumber();

  @Nullable
  SmithyShapeId getShapeId();

  @Nullable
  SmithyString getString();

  @Nullable
  SmithyTextBlock getTextBlock();

}
