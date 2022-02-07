// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiDocCommentBase;

public class SmithyVisitor extends PsiElementVisitor {

  public void visitApply(@NotNull SmithyApply o) {
    visitElement(o);
  }

  public void visitArray(@NotNull SmithyArray o) {
    visitElement(o);
  }

  public void visitBoolean(@NotNull SmithyBoolean o) {
    visitElement(o);
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
    visitElement(o);
  }

  public void visitListDefinition(@NotNull SmithyListDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitMapDefinition(@NotNull SmithyMapDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
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

  public void visitNamespace(@NotNull SmithyNamespace o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitNamespaceDefinition(@NotNull SmithyNamespaceDefinition o) {
    visitElement(o);
  }

  public void visitNull(@NotNull SmithyNull o) {
    visitElement(o);
  }

  public void visitNumber(@NotNull SmithyNumber o) {
    visitElement(o);
  }

  public void visitOperationDefinition(@NotNull SmithyOperationDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitPrimitive(@NotNull SmithyPrimitive o) {
    visitElement(o);
  }

  public void visitResourceDefinition(@NotNull SmithyResourceDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitServiceDefinition(@NotNull SmithyServiceDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitSetDefinition(@NotNull SmithySetDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitShapeId(@NotNull SmithyShapeId o) {
    visitElement(o);
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

  public void visitSimpleShapeDefinition(@NotNull SmithySimpleShapeDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitSimpleTypeName(@NotNull SmithySimpleTypeName o) {
    visitElement(o);
  }

  public void visitString(@NotNull SmithyString o) {
    visitElement(o);
  }

  public void visitStructure(@NotNull SmithyStructure o) {
    visitElement(o);
  }

  public void visitStructureDefinition(@NotNull SmithyStructureDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitSymbol(@NotNull SmithySymbol o) {
    visitElement(o);
  }

  public void visitTextBlock(@NotNull SmithyTextBlock o) {
    visitElement(o);
  }

  public void visitTrait(@NotNull SmithyTrait o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitTraitBody(@NotNull SmithyTraitBody o) {
    visitElement(o);
  }

  public void visitTraitValues(@NotNull SmithyTraitValues o) {
    visitElement(o);
  }

  public void visitUnionDefinition(@NotNull SmithyUnionDefinition o) {
    visitElement(o);
    // visitShapeDefinition(o);
  }

  public void visitValue(@NotNull SmithyValue o) {
    visitElement(o);
  }

  public void visitPsiDocCommentBase(@NotNull PsiDocCommentBase o) {
    visitElement(o);
  }

  public void visitElement(@NotNull SmithyElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
