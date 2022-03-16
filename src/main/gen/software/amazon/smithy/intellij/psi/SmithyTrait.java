// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyTrait extends SmithyNamedElement, SmithyElement {

  @NotNull
  SmithyShapeId getShapeId();

  @Nullable
  SmithyTraitBody getBody();

  @NotNull String getName();

  @NotNull SmithyShapeName getNameIdentifier();

  int getTextOffset();

  @NotNull SmithyTrait setName(String newName);

}
