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
import com.intellij.navigation.ItemPresentation;

public class SmithyMemberImpl extends SmithyPsiElement implements SmithyMember {

  public SmithyMemberImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitMember(this);
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
  public SmithyShapeId getShapeId() {
    return findNotNullChildByClass(SmithyShapeId.class);
  }

  @Override
  @NotNull
  public List<SmithyTrait> getDeclaredTraits() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SmithyTrait.class);
  }

  @Override
  @NotNull
  public SmithyMemberName getNameIdentifier() {
    return findNotNullChildByClass(SmithyMemberName.class);
  }

  @Override
  public @NotNull String getName() {
    return SmithyPsiImplUtil.getName(this);
  }

  @Override
  public @NotNull ItemPresentation getPresentation() {
    return SmithyPsiImplUtil.getPresentation(this);
  }

  @Override
  public int getTextOffset() {
    return SmithyPsiImplUtil.getTextOffset(this);
  }

  @Override
  public @NotNull SmithyMember setName(String newName) {
    return SmithyPsiImplUtil.setName(this, newName);
  }

}
