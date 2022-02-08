// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyShape extends SmithyElement {

  @NotNull String getNamespace();

  @NotNull String getName();

  @Nullable SmithyDocumentation getDocumentation();

  @NotNull List<SmithyTrait> getDeclaredTraits();

}