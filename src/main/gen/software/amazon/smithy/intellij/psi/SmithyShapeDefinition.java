package software.amazon.smithy.intellij.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A definition which introduces a new shape to the {@link SmithyModel}.
 *
 * @author Ian Caffey
 * @see SmithyListDefinition
 * @see SmithyMapDefinition
 * @see SmithyOperationDefinition
 * @see SmithyResourceDefinition
 * @see SmithyServiceDefinition
 * @see SmithySetDefinition
 * @see SmithySimpleShapeDefinition
 * @see SmithyStructureDefinition
 * @see SmithyUnionDefinition
 * @since 1.0
 */
public interface SmithyShapeDefinition extends SmithyElement {
    @Nullable
    SmithyDocumentation getDocumentation();

    @NotNull
    List<SmithyTrait> getTraitList();
}
