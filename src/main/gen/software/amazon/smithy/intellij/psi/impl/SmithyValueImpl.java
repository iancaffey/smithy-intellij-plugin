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

public class SmithyValueImpl extends ASTWrapperPsiElement implements SmithyValue {

  public SmithyValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SmithyVisitor visitor) {
    visitor.visitValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SmithyVisitor) accept((SmithyVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SmithyArray getArray() {
    return findChildByClass(SmithyArray.class);
  }

  @Override
  @Nullable
  public SmithyPrimitive getPrimitive() {
    return findChildByClass(SmithyPrimitive.class);
  }

  @Override
  @Nullable
  public SmithyStructure getStructure() {
    return findChildByClass(SmithyStructure.class);
  }

}