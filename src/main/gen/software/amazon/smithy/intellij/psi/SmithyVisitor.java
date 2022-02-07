// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiDocCommentBase;

public class SmithyVisitor extends PsiElementVisitor {

  public void visitApply(@NotNull SmithyApply o) {
    visitShapeStatement(o);
    // visitElement(o);
  }

  public void visitArray(@NotNull SmithyArray o) {
    visitValue(o);
    // visitElement(o);
  }

  public void visitBoolean(@NotNull SmithyBoolean o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitControlSection(@NotNull SmithyControlSection o) {
    visitElement(o);
  }

  public void visitControlStatement(@NotNull SmithyControlStatement o) {
    visitElement(o);
  }

  public void visitDocumentation(@NotNull SmithyDocumentation o) {
    visitPsiDocCommentBase(o);
    // visitElement(o);
  }

  public void visitElement(@NotNull SmithyElement o) {
    visitPsiElement(o);
  }

  public void visitEntry(@NotNull SmithyEntry o) {
    visitElement(o);
  }

  public void visitId(@NotNull SmithyId o) {
    visitElement(o);
  }

  public void visitImport(@NotNull SmithyImport o) {
    visitElement(o);
  }

  public void visitKey(@NotNull SmithyKey o) {
    visitElement(o);
  }

  public void visitKeyword(@NotNull SmithyKeyword o) {
    visitId(o);
    // visitElement(o);
  }

  public void visitList(@NotNull SmithyList o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitMap(@NotNull SmithyMap o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitMember(@NotNull SmithyMember o) {
    visitElement(o);
  }

  public void visitMemberName(@NotNull SmithyMemberName o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitMetadataSection(@NotNull SmithyMetadataSection o) {
    visitElement(o);
  }

  public void visitMetadataStatement(@NotNull SmithyMetadataStatement o) {
    visitElement(o);
  }

  public void visitModel(@NotNull SmithyModel o) {
    visitElement(o);
  }

  public void visitNamedElement(@NotNull SmithyNamedElement o) {
    visitPsiElement(o);
  }

  public void visitNamespace(@NotNull SmithyNamespace o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitNamespaceStatement(@NotNull SmithyNamespaceStatement o) {
    visitElement(o);
  }

  public void visitNull(@NotNull SmithyNull o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitNumber(@NotNull SmithyNumber o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitObject(@NotNull SmithyObject o) {
    visitValue(o);
    // visitElement(o);
  }

  public void visitOperation(@NotNull SmithyOperation o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitPrimitive(@NotNull SmithyPrimitive o) {
    visitValue(o);
    // visitElement(o);
  }

  public void visitResource(@NotNull SmithyResource o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitService(@NotNull SmithyService o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitSet(@NotNull SmithySet o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitShape(@NotNull SmithyShape o) {
    visitShapeStatement(o);
    // visitElement(o);
  }

  public void visitShapeId(@NotNull SmithyShapeId o) {
    visitPrimitive(o);
    // visitElement(o);
    // visitNamedElement(o);
  }

  public void visitShapeName(@NotNull SmithyShapeName o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitShapeSection(@NotNull SmithyShapeSection o) {
    visitElement(o);
  }

  public void visitShapeStatement(@NotNull SmithyShapeStatement o) {
    visitElement(o);
  }

  public void visitSimpleShape(@NotNull SmithySimpleShape o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitSimpleTypeName(@NotNull SmithySimpleTypeName o) {
    visitId(o);
    // visitElement(o);
  }

  public void visitString(@NotNull SmithyString o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitStructure(@NotNull SmithyStructure o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitSymbol(@NotNull SmithySymbol o) {
    visitId(o);
    // visitElement(o);
  }

  public void visitTextBlock(@NotNull SmithyTextBlock o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitTrait(@NotNull SmithyTrait o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitTraitArguments(@NotNull SmithyTraitArguments o) {
    visitElement(o);
  }

  public void visitTraitValues(@NotNull SmithyTraitValues o) {
    visitElement(o);
  }

  public void visitUnion(@NotNull SmithyUnion o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitValue(@NotNull SmithyValue o) {
    visitElement(o);
  }

  public void visitPsiDocCommentBase(@NotNull PsiDocCommentBase o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
