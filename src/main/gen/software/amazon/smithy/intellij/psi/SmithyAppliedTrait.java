// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.ext.SmithyElement;

public interface SmithyAppliedTrait extends SmithyElement {

  @Nullable
  SmithyMemberId getMemberId();

  @Nullable
  SmithyShapeId getShapeId();

  @NotNull
  SmithyTrait getTrait();

}
