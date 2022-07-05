// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyShapeReference.ById;

public interface SmithyShapeId extends SmithyPrimitive, SmithyNamedElement, SmithyElement {

  @Nullable
  SmithyMemberName getMemberName();

  @Nullable
  SmithyNamespaceId getNamespaceId();

  @NotNull
  SmithyShapeName getShapeName();

  @NotNull
  String getId();

  @NotNull
  String getName();

  @NotNull
  SmithyShapeId setName(@Nullable String newName);

  @NotNull
  SmithyShapeName getNameIdentifier();

  int getTextOffset();

  @Nullable
  String getDeclaredNamespace();

  @NotNull
  String getEnclosingNamespace();

  @NotNull
  ById getReference();

}
