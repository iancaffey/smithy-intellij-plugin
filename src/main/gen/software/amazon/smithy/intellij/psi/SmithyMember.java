// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyMember extends SmithyElement {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  SmithyMemberName getMemberName();

  @NotNull
  SmithyShapeId getShapeId();

  @NotNull
  List<SmithyTrait> getTraits();

}
