// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import software.amazon.smithy.intellij.ext.SmithyElement;
import software.amazon.smithy.intellij.ext.SmithyNumberExt;
import software.amazon.smithy.intellij.ext.SmithyContainer;
import software.amazon.smithy.intellij.ext.SmithyMemberExt;
import software.amazon.smithy.intellij.ext.SmithyTraitExt;
import software.amazon.smithy.intellij.ext.SmithyModelExt;
import software.amazon.smithy.intellij.ext.SmithyValueExt;
import software.amazon.smithy.intellij.ext.SmithyKeyExt;
import software.amazon.smithy.intellij.ext.SmithyMemberIdExt;
import software.amazon.smithy.intellij.ext.SmithyKeyedElementExt;
import software.amazon.smithy.intellij.ext.SmithyShapeExt;
import software.amazon.smithy.intellij.ext.SmithyDocumentationExt;
import software.amazon.smithy.intellij.ext.SmithyEntryExt;
import software.amazon.smithy.intellij.ext.SmithyBooleanExt;
import software.amazon.smithy.intellij.ext.SmithyNamespaceIdExt;
import software.amazon.smithy.intellij.ext.SmithyShapeIdExt;

public class SmithyVisitor extends PsiElementVisitor {

  public void visitAggregateShape(@NotNull SmithyAggregateShape o) {
    visitShape(o);
  }

  public void visitAppliedTrait(@NotNull SmithyAppliedTrait o) {
    visitElement(o);
  }

  public void visitArray(@NotNull SmithyArray o) {
    visitValue(o);
    // visitContainer(o);
  }

  public void visitBoolean(@NotNull SmithyBoolean o) {
    visitPrimitive(o);
    // visitBooleanExt(o);
  }

  public void visitControl(@NotNull SmithyControl o) {
    visitKeyedElement(o);
    // visitElement(o);
  }

  public void visitDocumentation(@NotNull SmithyDocumentation o) {
    visitDocumentationExt(o);
    // visitElement(o);
  }

  public void visitEntry(@NotNull SmithyEntry o) {
    visitKeyedElement(o);
    // visitEntryExt(o);
    // visitElement(o);
  }

  public void visitId(@NotNull SmithyId o) {
    visitElement(o);
  }

  public void visitImport(@NotNull SmithyImport o) {
    visitElement(o);
  }

  public void visitIncompleteAppliedTrait(@NotNull SmithyIncompleteAppliedTrait o) {
    visitElement(o);
  }

  public void visitIncompleteEntry(@NotNull SmithyIncompleteEntry o) {
    visitElement(o);
  }

  public void visitIncompleteMember(@NotNull SmithyIncompleteMember o) {
    visitElement(o);
  }

  public void visitKey(@NotNull SmithyKey o) {
    visitKeyExt(o);
    // visitElement(o);
  }

  public void visitKeyedElement(@NotNull SmithyKeyedElement o) {
    visitKeyedElementExt(o);
  }

  public void visitKeyword(@NotNull SmithyKeyword o) {
    visitId(o);
  }

  public void visitList(@NotNull SmithyList o) {
    visitAggregateShape(o);
  }

  public void visitMap(@NotNull SmithyMap o) {
    visitAggregateShape(o);
  }

  public void visitMember(@NotNull SmithyMember o) {
    visitMemberExt(o);
    // visitElement(o);
  }

  public void visitMemberId(@NotNull SmithyMemberId o) {
    visitMemberIdExt(o);
    // visitElement(o);
  }

  public void visitMemberName(@NotNull SmithyMemberName o) {
    visitElement(o);
  }

  public void visitMetadata(@NotNull SmithyMetadata o) {
    visitKeyedElement(o);
    // visitElement(o);
  }

  public void visitModel(@NotNull SmithyModel o) {
    visitModelExt(o);
    // visitElement(o);
  }

  public void visitNamespace(@NotNull SmithyNamespace o) {
    visitElement(o);
  }

  public void visitNamespaceId(@NotNull SmithyNamespaceId o) {
    visitNamespaceIdExt(o);
    // visitElement(o);
  }

  public void visitNull(@NotNull SmithyNull o) {
    visitPrimitive(o);
  }

  public void visitNumber(@NotNull SmithyNumber o) {
    visitPrimitive(o);
    // visitNumberExt(o);
  }

  public void visitObject(@NotNull SmithyObject o) {
    visitValue(o);
    // visitContainer(o);
  }

  public void visitOperation(@NotNull SmithyOperation o) {
    visitShape(o);
  }

  public void visitPrimitive(@NotNull SmithyPrimitive o) {
    visitValue(o);
  }

  public void visitResource(@NotNull SmithyResource o) {
    visitShape(o);
  }

  public void visitService(@NotNull SmithyService o) {
    visitShape(o);
  }

  public void visitSet(@NotNull SmithySet o) {
    visitAggregateShape(o);
  }

  public void visitShape(@NotNull SmithyShape o) {
    visitShapeExt(o);
    // visitElement(o);
  }

  public void visitShapeBody(@NotNull SmithyShapeBody o) {
    visitElement(o);
    // visitContainer(o);
  }

  public void visitShapeId(@NotNull SmithyShapeId o) {
    visitPrimitive(o);
    // visitShapeIdExt(o);
  }

  public void visitShapeName(@NotNull SmithyShapeName o) {
    visitElement(o);
  }

  public void visitSimpleShape(@NotNull SmithySimpleShape o) {
    visitShape(o);
  }

  public void visitSimpleTypeName(@NotNull SmithySimpleTypeName o) {
    visitId(o);
  }

  public void visitString(@NotNull SmithyString o) {
    visitPrimitive(o);
  }

  public void visitStructure(@NotNull SmithyStructure o) {
    visitAggregateShape(o);
  }

  public void visitSymbol(@NotNull SmithySymbol o) {
    visitId(o);
  }

  public void visitTextBlock(@NotNull SmithyTextBlock o) {
    visitPrimitive(o);
  }

  public void visitTrait(@NotNull SmithyTrait o) {
    visitTraitExt(o);
    // visitElement(o);
  }

  public void visitTraitBody(@NotNull SmithyTraitBody o) {
    visitElement(o);
    // visitContainer(o);
  }

  public void visitUnion(@NotNull SmithyUnion o) {
    visitAggregateShape(o);
  }

  public void visitValue(@NotNull SmithyValue o) {
    visitValueExt(o);
    // visitElement(o);
  }

  public void visitDocumentationExt(@NotNull SmithyDocumentationExt o) {
    visitPsiElement(o);
  }

  public void visitElement(@NotNull SmithyElement o) {
    visitPsiElement(o);
  }

  public void visitKeyExt(@NotNull SmithyKeyExt o) {
    visitPsiElement(o);
  }

  public void visitKeyedElementExt(@NotNull SmithyKeyedElementExt o) {
    visitPsiElement(o);
  }

  public void visitMemberExt(@NotNull SmithyMemberExt o) {
    visitPsiElement(o);
  }

  public void visitMemberIdExt(@NotNull SmithyMemberIdExt o) {
    visitPsiElement(o);
  }

  public void visitModelExt(@NotNull SmithyModelExt o) {
    visitPsiElement(o);
  }

  public void visitNamespaceIdExt(@NotNull SmithyNamespaceIdExt o) {
    visitPsiElement(o);
  }

  public void visitShapeExt(@NotNull SmithyShapeExt o) {
    visitPsiElement(o);
  }

  public void visitTraitExt(@NotNull SmithyTraitExt o) {
    visitPsiElement(o);
  }

  public void visitValueExt(@NotNull SmithyValueExt o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
