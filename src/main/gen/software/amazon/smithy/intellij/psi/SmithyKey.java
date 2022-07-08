// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyMemberReference;

public interface SmithyKey extends SmithyElement {

  @Nullable
  SmithyId getId();

  @Nullable
  SmithyString getString();

  @NotNull
  SmithyMemberReference getReference();

}
