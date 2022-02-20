// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyMember extends SmithyNamedElement, SmithyElement {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  SmithyShapeId getShapeId();

  @NotNull
  List<SmithyTrait> getDeclaredTraits();

  @NotNull
  SmithyMemberName getNameIdentifier();

  @NotNull String getName();

  int getTextOffset();

  @NotNull SmithyMember setName(String newName);

}
