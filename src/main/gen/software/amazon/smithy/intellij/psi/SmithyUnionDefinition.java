// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyUnionDefinition extends SmithyElement, SmithyShapeDefinition {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  List<SmithyMember> getMemberList();

  @NotNull
  SmithyShapeName getShapeName();

  @NotNull
  List<SmithyTrait> getTraitList();

}
