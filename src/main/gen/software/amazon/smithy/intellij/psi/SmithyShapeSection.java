// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyShapeSection extends PsiElement {

  @NotNull
  SmithyImports getImports();

  @NotNull
  SmithyNamespaceDefinition getNamespaceDefinition();

  @NotNull
  SmithyShapeStatements getShapeStatements();

}
