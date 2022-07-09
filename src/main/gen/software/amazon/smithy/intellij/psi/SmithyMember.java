// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.ext.SmithyMemberExt;
import software.amazon.smithy.intellij.ext.SmithyElement;

public interface SmithyMember extends SmithyMemberExt, SmithyElement {

  @Nullable
  SmithyDocumentation getDocumentation();

  @NotNull
  SmithyShapeId getShapeId();

  @NotNull
  List<SmithyTrait> getDeclaredTraits();

  @NotNull
  SmithyMemberName getNameIdentifier();

}
