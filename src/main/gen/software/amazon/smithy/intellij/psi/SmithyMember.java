// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyMemberDefinition;
import com.intellij.navigation.ItemPresentation;

public interface SmithyMember extends SmithyNamedElement, SmithyMemberDefinition, SmithyElement {

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

  @NotNull
  SmithyAggregateShape getEnclosingShape();

  @NotNull
  String getTargetShapeId();

}
