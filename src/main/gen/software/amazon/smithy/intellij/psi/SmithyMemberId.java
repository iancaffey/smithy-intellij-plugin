// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyMemberReference;

public interface SmithyMemberId extends SmithyElement {

  @NotNull
  SmithyMemberName getMemberName();

  @NotNull
  SmithyShapeId getShapeId();

  @NotNull
  String getId();

  @NotNull
  SmithyMemberReference getReference();

}
