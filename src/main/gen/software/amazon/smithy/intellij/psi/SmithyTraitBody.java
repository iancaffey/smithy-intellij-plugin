// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.ext.SmithyElement;
import software.amazon.smithy.intellij.ext.SmithyContainer;

public interface SmithyTraitBody extends SmithyElement, SmithyContainer {

  @Nullable
  SmithyValue getValue();

  @NotNull
  List<SmithyEntry> getValues();

}
