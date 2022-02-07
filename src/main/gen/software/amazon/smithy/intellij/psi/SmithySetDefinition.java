// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithySetDefinition extends SmithyShapeDefinition, SmithyElement {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  SmithyShapeName getShapeName();

  @NotNull
  List<SmithyTrait> getTraits();

  @NotNull
  List<SmithyMember> getMembers();

}
