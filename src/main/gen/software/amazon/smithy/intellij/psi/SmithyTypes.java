// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import software.amazon.smithy.intellij.SmithyElementType;
import software.amazon.smithy.intellij.SmithyTokenType;
import software.amazon.smithy.intellij.psi.impl.*;

public interface SmithyTypes {

  IElementType APPLIED_TRAIT = new SmithyElementType("APPLIED_TRAIT");
  IElementType ARRAY = new SmithyElementType("ARRAY");
  IElementType BOOLEAN = new SmithyElementType("BOOLEAN");
  IElementType CONTROL = new SmithyElementType("CONTROL");
  IElementType DOCUMENTATION = new SmithyElementType("DOCUMENTATION");
  IElementType ENTRY = new SmithyElementType("ENTRY");
  IElementType ID = new SmithyElementType("ID");
  IElementType IMPORT = new SmithyElementType("IMPORT");
  IElementType KEY = new SmithyElementType("KEY");
  IElementType KEYWORD = new SmithyElementType("KEYWORD");
  IElementType LIST = new SmithyElementType("LIST");
  IElementType MAP = new SmithyElementType("MAP");
  IElementType MEMBER = new SmithyElementType("MEMBER");
  IElementType MEMBER_NAME = new SmithyElementType("MEMBER_NAME");
  IElementType METADATA = new SmithyElementType("METADATA");
  IElementType MODEL = new SmithyElementType("MODEL");
  IElementType NAMESPACE = new SmithyElementType("NAMESPACE");
  IElementType NAMESPACE_ID = new SmithyElementType("NAMESPACE_ID");
  IElementType NULL = new SmithyElementType("NULL");
  IElementType NUMBER = new SmithyElementType("NUMBER");
  IElementType OBJECT = new SmithyElementType("OBJECT");
  IElementType OPERATION = new SmithyElementType("OPERATION");
  IElementType PRIMITIVE = new SmithyElementType("PRIMITIVE");
  IElementType RESOURCE = new SmithyElementType("RESOURCE");
  IElementType SERVICE = new SmithyElementType("SERVICE");
  IElementType SET = new SmithyElementType("SET");
  IElementType SHAPE = new SmithyElementType("SHAPE");
  IElementType SHAPE_BODY = new SmithyElementType("SHAPE_BODY");
  IElementType SHAPE_ID = new SmithyElementType("SHAPE_ID");
  IElementType SHAPE_NAME = new SmithyElementType("SHAPE_NAME");
  IElementType SIMPLE_SHAPE = new SmithyElementType("SIMPLE_SHAPE");
  IElementType SIMPLE_TYPE_NAME = new SmithyElementType("SIMPLE_TYPE_NAME");
  IElementType STRING = new SmithyElementType("STRING");
  IElementType STRUCTURE = new SmithyElementType("STRUCTURE");
  IElementType SYMBOL = new SmithyElementType("SYMBOL");
  IElementType TEXT_BLOCK = new SmithyElementType("TEXT_BLOCK");
  IElementType TRAIT = new SmithyElementType("TRAIT");
  IElementType TRAIT_BODY = new SmithyElementType("TRAIT_BODY");
  IElementType UNION = new SmithyElementType("UNION");
  IElementType VALUE = new SmithyElementType("VALUE");

  IElementType TOKEN_APPLY = new SmithyTokenType("apply");
  IElementType TOKEN_AT = new SmithyTokenType("@");
  IElementType TOKEN_BOOLEAN = new SmithyTokenType("TOKEN_BOOLEAN");
  IElementType TOKEN_CLOSE_BRACE = new SmithyTokenType("}");
  IElementType TOKEN_CLOSE_BRACKET = new SmithyTokenType("]");
  IElementType TOKEN_CLOSE_PAREN = new SmithyTokenType(")");
  IElementType TOKEN_COLON = new SmithyTokenType(":");
  IElementType TOKEN_COMMA = new SmithyTokenType(",");
  IElementType TOKEN_DOCUMENTATION_LINE = new SmithyTokenType("TOKEN_DOCUMENTATION_LINE");
  IElementType TOKEN_DOLLAR_SIGN = new SmithyTokenType("$");
  IElementType TOKEN_EQUALS = new SmithyTokenType("=");
  IElementType TOKEN_HASH = new SmithyTokenType("#");
  IElementType TOKEN_INCOMPLETE_STRING = new SmithyTokenType("TOKEN_INCOMPLETE_STRING");
  IElementType TOKEN_INCOMPLETE_TEXT_BLOCK = new SmithyTokenType("TOKEN_INCOMPLETE_TEXT_BLOCK");
  IElementType TOKEN_LINE_COMMENT = new SmithyTokenType("TOKEN_LINE_COMMENT");
  IElementType TOKEN_LIST = new SmithyTokenType("list");
  IElementType TOKEN_MAP = new SmithyTokenType("map");
  IElementType TOKEN_METADATA = new SmithyTokenType("metadata");
  IElementType TOKEN_NAMESPACE = new SmithyTokenType("namespace");
  IElementType TOKEN_NULL = new SmithyTokenType("null");
  IElementType TOKEN_NUMBER = new SmithyTokenType("TOKEN_NUMBER");
  IElementType TOKEN_OPEN_BRACE = new SmithyTokenType("{");
  IElementType TOKEN_OPEN_BRACKET = new SmithyTokenType("[");
  IElementType TOKEN_OPEN_PAREN = new SmithyTokenType("(");
  IElementType TOKEN_OPERATION = new SmithyTokenType("operation");
  IElementType TOKEN_PERIOD = new SmithyTokenType(".");
  IElementType TOKEN_RESOURCE = new SmithyTokenType("resource");
  IElementType TOKEN_SERVICE = new SmithyTokenType("service");
  IElementType TOKEN_SET = new SmithyTokenType("set");
  IElementType TOKEN_SIMPLE_TYPE_NAME = new SmithyTokenType("TOKEN_SIMPLE_TYPE_NAME");
  IElementType TOKEN_STRING = new SmithyTokenType("TOKEN_STRING");
  IElementType TOKEN_STRUCTURE = new SmithyTokenType("structure");
  IElementType TOKEN_SYMBOL = new SmithyTokenType("TOKEN_SYMBOL");
  IElementType TOKEN_TEXT_BLOCK = new SmithyTokenType("TOKEN_TEXT_BLOCK");
  IElementType TOKEN_UNION = new SmithyTokenType("union");
  IElementType TOKEN_USE = new SmithyTokenType("use");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == APPLIED_TRAIT) {
        return new SmithyAppliedTraitImpl(node);
      }
      else if (type == ARRAY) {
        return new SmithyArrayImpl(node);
      }
      else if (type == BOOLEAN) {
        return new SmithyBooleanImpl(node);
      }
      else if (type == CONTROL) {
        return new SmithyControlImpl(node);
      }
      else if (type == DOCUMENTATION) {
        return new SmithyDocumentationImpl(node);
      }
      else if (type == ENTRY) {
        return new SmithyEntryImpl(node);
      }
      else if (type == ID) {
        return new SmithyIdImpl(node);
      }
      else if (type == IMPORT) {
        return new SmithyImportImpl(node);
      }
      else if (type == KEY) {
        return new SmithyKeyImpl(node);
      }
      else if (type == KEYWORD) {
        return new SmithyKeywordImpl(node);
      }
      else if (type == LIST) {
        return new SmithyListImpl(node);
      }
      else if (type == MAP) {
        return new SmithyMapImpl(node);
      }
      else if (type == MEMBER) {
        return new SmithyMemberImpl(node);
      }
      else if (type == MEMBER_NAME) {
        return new SmithyMemberNameImpl(node);
      }
      else if (type == METADATA) {
        return new SmithyMetadataImpl(node);
      }
      else if (type == MODEL) {
        return new SmithyModelImpl(node);
      }
      else if (type == NAMESPACE) {
        return new SmithyNamespaceImpl(node);
      }
      else if (type == NAMESPACE_ID) {
        return new SmithyNamespaceIdImpl(node);
      }
      else if (type == NULL) {
        return new SmithyNullImpl(node);
      }
      else if (type == NUMBER) {
        return new SmithyNumberImpl(node);
      }
      else if (type == OBJECT) {
        return new SmithyObjectImpl(node);
      }
      else if (type == OPERATION) {
        return new SmithyOperationImpl(node);
      }
      else if (type == RESOURCE) {
        return new SmithyResourceImpl(node);
      }
      else if (type == SERVICE) {
        return new SmithyServiceImpl(node);
      }
      else if (type == SET) {
        return new SmithySetImpl(node);
      }
      else if (type == SHAPE_BODY) {
        return new SmithyShapeBodyImpl(node);
      }
      else if (type == SHAPE_ID) {
        return new SmithyShapeIdImpl(node);
      }
      else if (type == SHAPE_NAME) {
        return new SmithyShapeNameImpl(node);
      }
      else if (type == SIMPLE_SHAPE) {
        return new SmithySimpleShapeImpl(node);
      }
      else if (type == SIMPLE_TYPE_NAME) {
        return new SmithySimpleTypeNameImpl(node);
      }
      else if (type == STRING) {
        return new SmithyStringImpl(node);
      }
      else if (type == STRUCTURE) {
        return new SmithyStructureImpl(node);
      }
      else if (type == SYMBOL) {
        return new SmithySymbolImpl(node);
      }
      else if (type == TEXT_BLOCK) {
        return new SmithyTextBlockImpl(node);
      }
      else if (type == TRAIT) {
        return new SmithyTraitImpl(node);
      }
      else if (type == TRAIT_BODY) {
        return new SmithyTraitBodyImpl(node);
      }
      else if (type == UNION) {
        return new SmithyUnionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
