// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyModel extends SmithyElement {

  @NotNull
  SmithyControlSection getControlSection();

  @NotNull
  SmithyMetadataSection getMetadataSection();

  @Nullable
  SmithyShapeSection getShapeSection();

}