// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyKeyedElement extends SmithyNamedElement {

  @NotNull
  SmithyKey getKey();

  @NotNull
  String getName();

  @NotNull
  SmithyKeyedElement setName(@Nullable String newName);

  @NotNull
  SmithyKey getNameIdentifier();

  int getTextOffset();

}
