// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import software.amazon.smithy.intellij.psi.*;

public class SmithyStructureDefinitionImpl extends SmithyShapeDefinitionImpl implements SmithyStructureDefinition {

  public SmithyStructureDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitStructureDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyDocumentation getDocumentation() {
    return findChildByClass(SmithyDocumentation.class);
  }

  @Override
  @NotNull
  public SmithyShapeName getShapeName() {
    return findNotNullChildByClass(SmithyShapeName.class);
  }

  @Override
  @NotNull
  public List<SmithyTrait> getTraits() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyTrait.class);
  }

  @Override
  @NotNull
  public List<SmithyMember> getMembers() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyMember.class);
  }

}
