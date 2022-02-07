// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyResourceDefinition extends SmithyElement, SmithyShapeDefinition {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  SmithyShapeName getShapeName();

  @NotNull
  SmithyStructure getStructure();

  @NotNull
  List<SmithyTrait> getTraitList();

}
