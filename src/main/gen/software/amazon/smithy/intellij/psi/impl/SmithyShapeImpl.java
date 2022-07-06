// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import software.amazon.smithy.intellij.ext.SmithyPsiElement;
import software.amazon.smithy.intellij.psi.*;
import software.amazon.smithy.intellij.ext.SmithyPsiImplUtilKt;
import com.intellij.navigation.ItemPresentation;

public abstract class SmithyShapeImpl extends SmithyPsiElement implements SmithyShape {

  public SmithyShapeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShape(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public String getNamespace() {
    return SmithyPsiImplUtilKt.getNamespace(this);
  }

  @Override
  @NotNull
  public String getName() {
    return SmithyPsiImplUtilKt.getName(this);
  }

  @Override
  @NotNull
  public String getShapeId() {
    return SmithyPsiImplUtilKt.getShapeId(this);
  }

  @Override
  @Nullable
  public SmithyDocumentation getDocumentation() {
    return SmithyPsiImplUtilKt.getDocumentation(this);
  }

  @Override
  @NotNull
  public List<SmithyTrait> getDeclaredTraits() {
    return SmithyPsiImplUtilKt.getDeclaredTraits(this);
  }

  @Override
  @NotNull
  public SmithyShapeName getNameIdentifier() {
    return SmithyPsiImplUtilKt.getNameIdentifier(this);
  }

  @Override
  @NotNull
  public ItemPresentation getPresentation() {
    return SmithyPsiImplUtilKt.getPresentation(this);
  }

  @Override
  public int getTextOffset() {
    return SmithyPsiImplUtilKt.getTextOffset(this);
  }

  @Override
  @NotNull
  public SmithyShape setName(@Nullable String newName) {
    return SmithyPsiImplUtilKt.setName(this, newName);
  }

  @Override
  @Nullable
  public SmithyMember getMember(@NotNull String name) {
    return SmithyPsiImplUtilKt.getMember(this, name);
  }

  @Override
  @NotNull
  public List<SmithyMember> getMembers() {
    return SmithyPsiImplUtilKt.getMembers(this);
  }

}
