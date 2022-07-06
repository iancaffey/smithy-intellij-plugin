// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyShapeDefinition;
import com.intellij.navigation.ItemPresentation;

public interface SmithyShape extends SmithyNamedElement, SmithyShapeDefinition, SmithyElement {

  @NotNull
  String getNamespace();

  @NotNull
  String getName();

  @NotNull
  String getShapeId();

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

  @Nullable
  SmithyMember getMember(@NotNull String name);

  @NotNull
  List<SmithyMember> getMembers();

}
