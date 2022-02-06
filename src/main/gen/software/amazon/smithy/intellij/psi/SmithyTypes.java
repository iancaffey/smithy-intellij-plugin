// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import software.amazon.smithy.intellij.SmithyElementType;
import software.amazon.smithy.intellij.SmithyTokenType;
import software.amazon.smithy.intellij.psi.impl.*;

public interface SmithyTypes {

  IElementType APPLY = new SmithyElementType("APPLY");
  IElementType ARRAY = new SmithyElementType("ARRAY");
  IElementType BOOLEAN = new SmithyElementType("BOOLEAN");
  IElementType CONTROL_DEFINITION = new SmithyElementType("CONTROL_DEFINITION");
  IElementType CONTROL_SECTION = new SmithyElementType("CONTROL_SECTION");
  IElementType DOCUMENTATION = new SmithyElementType("DOCUMENTATION");
  IElementType ENTRY = new SmithyElementType("ENTRY");
  IElementType ID = new SmithyElementType("ID");
  IElementType IMPORT = new SmithyElementType("IMPORT");
  IElementType KEY = new SmithyElementType("KEY");
  IElementType KEYWORD = new SmithyElementType("KEYWORD");
  IElementType LIST_DEFINITION = new SmithyElementType("LIST_DEFINITION");
  IElementType MAP_DEFINITION = new SmithyElementType("MAP_DEFINITION");
  IElementType MEMBER_NAME = new SmithyElementType("MEMBER_NAME");
  IElementType METADATA_DEFINITION = new SmithyElementType("METADATA_DEFINITION");
  IElementType METADATA_SECTION = new SmithyElementType("METADATA_SECTION");
  IElementType MODEL = new SmithyElementType("MODEL");
  IElementType NAMESPACE = new SmithyElementType("NAMESPACE");
  IElementType NAMESPACE_DEFINITION = new SmithyElementType("NAMESPACE_DEFINITION");
  IElementType NULL = new SmithyElementType("NULL");
  IElementType NUMBER = new SmithyElementType("NUMBER");
  IElementType OPERATION_DEFINITION = new SmithyElementType("OPERATION_DEFINITION");
  IElementType PRIMITIVE = new SmithyElementType("PRIMITIVE");
  IElementType RESOURCE_DEFINITION = new SmithyElementType("RESOURCE_DEFINITION");
  IElementType SERVICE_DEFINITION = new SmithyElementType("SERVICE_DEFINITION");
  IElementType SET_DEFINITION = new SmithyElementType("SET_DEFINITION");
  IElementType SHAPE_FIELD = new SmithyElementType("SHAPE_FIELD");
  IElementType SHAPE_FIELDS = new SmithyElementType("SHAPE_FIELDS");
  IElementType SHAPE_ID = new SmithyElementType("SHAPE_ID");
  IElementType SHAPE_NAME = new SmithyElementType("SHAPE_NAME");
  IElementType SHAPE_SECTION = new SmithyElementType("SHAPE_SECTION");
  IElementType SHAPE_STATEMENT = new SmithyElementType("SHAPE_STATEMENT");
  IElementType SIMPLE_SHAPE_DEFINITION = new SmithyElementType("SIMPLE_SHAPE_DEFINITION");
  IElementType SIMPLE_TYPE_NAME = new SmithyElementType("SIMPLE_TYPE_NAME");
  IElementType STRING = new SmithyElementType("STRING");
  IElementType STRUCTURE = new SmithyElementType("STRUCTURE");
  IElementType STRUCTURE_DEFINITION = new SmithyElementType("STRUCTURE_DEFINITION");
  IElementType SYMBOL = new SmithyElementType("SYMBOL");
  IElementType TEXT_BLOCK = new SmithyElementType("TEXT_BLOCK");
  IElementType TRAIT = new SmithyElementType("TRAIT");
  IElementType TRAIT_BODY = new SmithyElementType("TRAIT_BODY");
  IElementType TRAIT_VALUES = new SmithyElementType("TRAIT_VALUES");
  IElementType UNION_DEFINITION = new SmithyElementType("UNION_DEFINITION");
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
      if (type == APPLY) {
        return new SmithyApplyImpl(node);
      }
      else if (type == ARRAY) {
        return new SmithyArrayImpl(node);
      }
      else if (type == BOOLEAN) {
        return new SmithyBooleanImpl(node);
      }
      else if (type == CONTROL_DEFINITION) {
        return new SmithyControlDefinitionImpl(node);
      }
      else if (type == CONTROL_SECTION) {
        return new SmithyControlSectionImpl(node);
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
      else if (type == LIST_DEFINITION) {
        return new SmithyListDefinitionImpl(node);
      }
      else if (type == MAP_DEFINITION) {
        return new SmithyMapDefinitionImpl(node);
      }
      else if (type == MEMBER_NAME) {
        return new SmithyMemberNameImpl(node);
      }
      else if (type == METADATA_DEFINITION) {
        return new SmithyMetadataDefinitionImpl(node);
      }
      else if (type == METADATA_SECTION) {
        return new SmithyMetadataSectionImpl(node);
      }
      else if (type == MODEL) {
        return new SmithyModelImpl(node);
      }
      else if (type == NAMESPACE) {
        return new SmithyNamespaceImpl(node);
      }
      else if (type == NAMESPACE_DEFINITION) {
        return new SmithyNamespaceDefinitionImpl(node);
      }
      else if (type == NULL) {
        return new SmithyNullImpl(node);
      }
      else if (type == NUMBER) {
        return new SmithyNumberImpl(node);
      }
      else if (type == OPERATION_DEFINITION) {
        return new SmithyOperationDefinitionImpl(node);
      }
      else if (type == PRIMITIVE) {
        return new SmithyPrimitiveImpl(node);
      }
      else if (type == RESOURCE_DEFINITION) {
        return new SmithyResourceDefinitionImpl(node);
      }
      else if (type == SERVICE_DEFINITION) {
        return new SmithyServiceDefinitionImpl(node);
      }
      else if (type == SET_DEFINITION) {
        return new SmithySetDefinitionImpl(node);
      }
      else if (type == SHAPE_FIELD) {
        return new SmithyShapeFieldImpl(node);
      }
      else if (type == SHAPE_FIELDS) {
        return new SmithyShapeFieldsImpl(node);
      }
      else if (type == SHAPE_ID) {
        return new SmithyShapeIdImpl(node);
      }
      else if (type == SHAPE_NAME) {
        return new SmithyShapeNameImpl(node);
      }
      else if (type == SHAPE_SECTION) {
        return new SmithyShapeSectionImpl(node);
      }
      else if (type == SHAPE_STATEMENT) {
        return new SmithyShapeStatementImpl(node);
      }
      else if (type == SIMPLE_SHAPE_DEFINITION) {
        return new SmithySimpleShapeDefinitionImpl(node);
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
      else if (type == STRUCTURE_DEFINITION) {
        return new SmithyStructureDefinitionImpl(node);
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
      else if (type == TRAIT_VALUES) {
        return new SmithyTraitValuesImpl(node);
      }
      else if (type == UNION_DEFINITION) {
        return new SmithyUnionDefinitionImpl(node);
      }
      else if (type == VALUE) {
        return new SmithyValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
