// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import software.amazon.smithy.intellij.psi.*;

public class SmithyShapeDefinitionImpl extends ASTWrapperPsiElement implements SmithyShapeDefinition {

  public SmithyShapeDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShapeDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyListDefinition getListDefinition() {
    return findChildByClass(SmithyListDefinition.class);
  }

  @Override
  @Nullable
  public SmithyMapDefinition getMapDefinition() {
    return findChildByClass(SmithyMapDefinition.class);
  }

  @Override
  @Nullable
  public SmithyOperationDefinition getOperationDefinition() {
    return findChildByClass(SmithyOperationDefinition.class);
  }

  @Override
  @Nullable
  public SmithyResourceDefinition getResourceDefinition() {
    return findChildByClass(SmithyResourceDefinition.class);
  }

  @Override
  @Nullable
  public SmithyServiceDefinition getServiceDefinition() {
    return findChildByClass(SmithyServiceDefinition.class);
  }

  @Override
  @Nullable
  public SmithySetDefinition getSetDefinition() {
    return findChildByClass(SmithySetDefinition.class);
  }

  @Override
  @Nullable
  public SmithySimpleShapeDefinition getSimpleShapeDefinition() {
    return findChildByClass(SmithySimpleShapeDefinition.class);
  }

  @Override
  @Nullable
  public SmithyStructureDefinition getStructureDefinition() {
    return findChildByClass(SmithyStructureDefinition.class);
  }

  @Override
  @NotNull
  public SmithyTraits getTraits() {
    return findNotNullChildByClass(SmithyTraits.class);
  }

  @Override
  @Nullable
  public SmithyUnionDefinition getUnionDefinition() {
    return findChildByClass(SmithyUnionDefinition.class);
  }

}
