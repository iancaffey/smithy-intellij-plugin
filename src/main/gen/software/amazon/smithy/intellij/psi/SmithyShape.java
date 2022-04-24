// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.navigation.ItemPresentation;

public interface SmithyShape extends SmithyNamedElement, NavigatablePsiElement, SmithyElement {

  @NotNull
  String getNamespace();

  @NotNull
  String getName();

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  List<SmithyTrait> getDeclaredTraits();

  @NotNull
  SmithyShapeName getNameIdentifier();

  @NotNull
  ItemPresentation getPresentation();

  int getTextOffset();

  @NotNull
  SmithyShape setName(@Nullable String newName);

}
