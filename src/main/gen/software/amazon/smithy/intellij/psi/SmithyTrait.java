// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.SmithyShapeDefinition;

public interface SmithyTrait extends SmithyElement {

  @NotNull
  SmithyShapeId getShapeId();

  @Nullable
  SmithyTraitBody getBody();

  @Nullable
  SmithyShapeDefinition resolve();

}
