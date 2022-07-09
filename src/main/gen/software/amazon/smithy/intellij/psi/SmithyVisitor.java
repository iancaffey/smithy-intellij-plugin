// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import software.amazon.smithy.intellij.SmithyMemberDefinition;
import software.amazon.smithy.intellij.SmithyShapeDefinition;
import com.intellij.psi.PsiDocCommentBase;

public class SmithyVisitor extends PsiElementVisitor {

  public void visitAggregateShape(@NotNull SmithyAggregateShape o) {
    visitShape(o);
    // visitElement(o);
  }

  public void visitAppliedTrait(@NotNull SmithyAppliedTrait o) {
    visitElement(o);
  }

  public void visitArray(@NotNull SmithyArray o) {
    visitValue(o);
    // visitElement(o);
    // visitContainer(o);
  }

  public void visitBoolean(@NotNull SmithyBoolean o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitContainer(@NotNull SmithyContainer o) {
    visitElement(o);
  }

  public void visitControl(@NotNull SmithyControl o) {
    visitKeyedElement(o);
    // visitElement(o);
  }

  public void visitDocumentation(@NotNull SmithyDocumentation o) {
    visitPsiDocCommentBase(o);
    // visitElement(o);
  }

  public void visitElement(@NotNull SmithyElement o) {
    visitPsiElement(o);
  }

  public void visitEntry(@NotNull SmithyEntry o) {
    visitKeyedElement(o);
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
    visitElement(o);
  }

  public void visitKeyedElement(@NotNull SmithyKeyedElement o) {
    visitNamedElement(o);
  }

  public void visitKeyword(@NotNull SmithyKeyword o) {
    visitId(o);
    // visitElement(o);
  }

  public void visitList(@NotNull SmithyList o) {
    visitAggregateShape(o);
    // visitElement(o);
  }

  public void visitMap(@NotNull SmithyMap o) {
    visitAggregateShape(o);
    // visitElement(o);
  }

  public void visitMember(@NotNull SmithyMember o) {
    visitNamedElement(o);
    // visitMemberDefinition(o);
    // visitElement(o);
  }

  public void visitMemberId(@NotNull SmithyMemberId o) {
    visitElement(o);
  }

  public void visitMemberName(@NotNull SmithyMemberName o) {
    visitElement(o);
  }

  public void visitMetadata(@NotNull SmithyMetadata o) {
    visitKeyedElement(o);
    // visitElement(o);
  }

  public void visitModel(@NotNull SmithyModel o) {
    visitElement(o);
  }

  public void visitNamedElement(@NotNull SmithyNamedElement o) {
    visitPsiNameIdentifierOwner(o);
  }

  public void visitNamespace(@NotNull SmithyNamespace o) {
    visitElement(o);
  }

  public void visitNamespaceId(@NotNull SmithyNamespaceId o) {
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
    // visitContainer(o);
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
    visitAggregateShape(o);
    // visitElement(o);
  }

  public void visitShape(@NotNull SmithyShape o) {
    visitNamedElement(o);
    // visitShapeDefinition(o);
    // visitElement(o);
  }

  public void visitShapeBody(@NotNull SmithyShapeBody o) {
    visitElement(o);
    // visitContainer(o);
  }

  public void visitShapeId(@NotNull SmithyShapeId o) {
    visitPrimitive(o);
    // visitElement(o);
  }

  public void visitShapeName(@NotNull SmithyShapeName o) {
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
    visitAggregateShape(o);
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
  }

  public void visitTraitBody(@NotNull SmithyTraitBody o) {
    visitElement(o);
    // visitContainer(o);
  }

  public void visitUnion(@NotNull SmithyUnion o) {
    visitAggregateShape(o);
    // visitElement(o);
  }

  public void visitValue(@NotNull SmithyValue o) {
    visitElement(o);
  }

  public void visitPsiDocCommentBase(@NotNull PsiDocCommentBase o) {
    visitElement(o);
  }

  public void visitPsiNameIdentifierOwner(@NotNull PsiNameIdentifierOwner o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
