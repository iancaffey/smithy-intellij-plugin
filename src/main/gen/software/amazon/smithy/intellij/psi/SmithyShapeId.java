// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyShapeReference;

public interface SmithyShapeId extends SmithyPrimitive, SmithyElement {

  @Nullable
  SmithyMemberName getMemberName();

  @Nullable
  SmithyNamespaceId getNamespaceId();

  @NotNull
  String getId();

  @NotNull
  String getShapeName();

  @Nullable
  String getDeclaredNamespace();

  @NotNull
  String getEnclosingNamespace();

  @NotNull
  SmithyShapeReference getReference();

}
