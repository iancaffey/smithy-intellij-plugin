// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SmithyShapeStatement extends SmithyElement {

  @Nullable
  SmithyApply getApply();

  @Nullable
  SmithyListDefinition getListDefinition();

  @Nullable
  SmithyMapDefinition getMapDefinition();

  @Nullable
  SmithyOperationDefinition getOperationDefinition();

  @Nullable
  SmithyResourceDefinition getResourceDefinition();

  @Nullable
  SmithyServiceDefinition getServiceDefinition();

  @Nullable
  SmithySetDefinition getSetDefinition();

  @Nullable
  SmithySimpleShapeDefinition getSimpleShapeDefinition();

  @Nullable
  SmithyStructureDefinition getStructureDefinition();

  @Nullable
  SmithyUnionDefinition getUnionDefinition();

}
