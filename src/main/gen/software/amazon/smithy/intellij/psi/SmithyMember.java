// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.navigation.ItemPresentation;

public interface SmithyMember extends SmithyNamedElement, NavigatablePsiElement, SmithyElement {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  SmithyShapeId getShapeId();

  @NotNull
  List<SmithyTrait> getDeclaredTraits();

  @NotNull
  SmithyMemberName getNameIdentifier();

  @NotNull
  String getName();

  @NotNull
  ItemPresentation getPresentation();

  int getTextOffset();

  @NotNull
  SmithyMember setName(@Nullable String newName);

}
