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
import software.amazon.smithy.intellij.ext.SmithyPsiImplUtilKt;
import software.amazon.smithy.intellij.SmithyShapeReference.ById;

public class SmithyShapeIdImpl extends SmithyPrimitiveImpl implements SmithyShapeId {

  public SmithyShapeIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitShapeId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyMemberName getMemberName() {
    return findChildByClass(SmithyMemberName.class);
  }

  @Override
  @Nullable
  public SmithyNamespaceId getNamespaceId() {
    return findChildByClass(SmithyNamespaceId.class);
  }

  @Override
  @NotNull
  public SmithyShapeName getShapeName() {
    return findNotNullChildByClass(SmithyShapeName.class);
  }

  @Override
  @NotNull
  public String getId() {
    return SmithyPsiImplUtilKt.getId(this);
  }

  @Override
  @NotNull
  public String getName() {
    return SmithyPsiImplUtilKt.getName(this);
  }

  @Override
  @NotNull
  public SmithyShapeId setName(@Nullable String newName) {
    return SmithyPsiImplUtilKt.setName(this, newName);
  }

  @Override
  @NotNull
  public SmithyShapeName getNameIdentifier() {
    return SmithyPsiImplUtilKt.getNameIdentifier(this);
  }

  @Override
  public int getTextOffset() {
    return SmithyPsiImplUtilKt.getTextOffset(this);
  }

  @Override
  @Nullable
  public String getDeclaredNamespace() {
    return SmithyPsiImplUtilKt.getDeclaredNamespace(this);
  }

  @Override
  @NotNull
  public String getEnclosingNamespace() {
    return SmithyPsiImplUtilKt.getEnclosingNamespace(this);
  }

  @Override
  @NotNull
  public ById getReference() {
    return SmithyPsiImplUtilKt.getReference(this);
  }

  @Override
  @NotNull
  public String toString() {
    return SmithyPsiImplUtilKt.toString(this);
  }

}
