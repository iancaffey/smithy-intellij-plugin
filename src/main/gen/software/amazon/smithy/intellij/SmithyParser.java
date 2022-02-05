// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import static software.amazon.smithy.intellij.SmithyParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SmithyParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  /* ********************************************************** */
  // TOKEN_APPLY shape_id trait
  public static boolean apply(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "apply")) return false;
    if (!nextTokenIs(b, TOKEN_APPLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_APPLY);
    r = r && shape_id(b, l + 1);
    r = r && trait(b, l + 1);
    exit_section_(b, m, APPLY, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_OPEN_BRACKET [value (TOKEN_COMMA value)* [TOKEN_COMMA]] TOKEN_CLOSE_BRACKET
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, TOKEN_OPEN_BRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPEN_BRACKET);
    r = r && array_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_CLOSE_BRACKET);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // [value (TOKEN_COMMA value)* [TOKEN_COMMA]]
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    array_1_0(b, l + 1);
    return true;
  }

  // value (TOKEN_COMMA value)* [TOKEN_COMMA]
  private static boolean array_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value(b, l + 1);
    r = r && array_1_0_1(b, l + 1);
    r = r && array_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TOKEN_COMMA value)*
  private static boolean array_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_1_0_1", c)) break;
    }
    return true;
  }

  // TOKEN_COMMA value
  private static boolean array_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_COMMA);
    r = r && value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_COMMA]
  private static boolean array_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_2")) return false;
    consumeToken(b, TOKEN_COMMA);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_BOOLEAN
  public static boolean boolean_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_$")) return false;
    if (!nextTokenIs(b, TOKEN_BOOLEAN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_BOOLEAN);
    exit_section_(b, m, BOOLEAN, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_DOLLAR_SIGN key TOKEN_COLON value
  public static boolean control_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control_definition")) return false;
    if (!nextTokenIs(b, TOKEN_DOLLAR_SIGN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_DOLLAR_SIGN);
    r = r && key(b, l + 1);
    r = r && consumeToken(b, TOKEN_COLON);
    r = r && value(b, l + 1);
    exit_section_(b, m, CONTROL_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // control_definition*
  public static boolean control_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control_section")) return false;
    Marker m = enter_section_(b, l, _NONE_, CONTROL_SECTION, "<control section>");
    while (true) {
      int c = current_position_(b);
      if (!control_definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "control_section", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // documentation_lines
  public static boolean documentation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "documentation")) return false;
    if (!nextTokenIs(b, TOKEN_DOCUMENTATION_LINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = documentation_lines(b, l + 1);
    exit_section_(b, m, DOCUMENTATION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_DOCUMENTATION_LINE+
  static boolean documentation_lines(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "documentation_lines")) return false;
    if (!nextTokenIs(b, TOKEN_DOCUMENTATION_LINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_DOCUMENTATION_LINE);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TOKEN_DOCUMENTATION_LINE)) break;
      if (!empty_element_parsed_guard_(b, "documentation_lines", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // key TOKEN_COLON value
  public static boolean entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entry")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENTRY, "<entry>");
    r = key(b, l + 1);
    r = r && consumeToken(b, TOKEN_COLON);
    r = r && value(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // symbol | keyword | null | boolean | simple_type_name
  public static boolean id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "id")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ID, "<id>");
    r = symbol(b, l + 1);
    if (!r) r = keyword(b, l + 1);
    if (!r) r = null_$(b, l + 1);
    if (!r) r = boolean_$(b, l + 1);
    if (!r) r = simple_type_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_USE root_shape_id
  public static boolean import_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_$")) return false;
    if (!nextTokenIs(b, TOKEN_USE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_USE);
    r = r && root_shape_id(b, l + 1);
    exit_section_(b, m, IMPORT, r);
    return r;
  }

  /* ********************************************************** */
  // import*
  public static boolean imports(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "imports")) return false;
    Marker m = enter_section_(b, l, _NONE_, IMPORTS, "<imports>");
    while (true) {
      int c = current_position_(b);
      if (!import_$(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "imports", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // id | string
  public static boolean key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KEY, "<key>");
    r = id(b, l + 1);
    if (!r) r = string(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_APPLY | TOKEN_USE | TOKEN_LIST | TOKEN_SET | TOKEN_MAP | TOKEN_METADATA | TOKEN_NAMESPACE | TOKEN_STRUCTURE | TOKEN_UNION | TOKEN_SERVICE | TOKEN_OPERATION | TOKEN_RESOURCE
  public static boolean keyword(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "keyword")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KEYWORD, "<keyword>");
    r = consumeToken(b, TOKEN_APPLY);
    if (!r) r = consumeToken(b, TOKEN_USE);
    if (!r) r = consumeToken(b, TOKEN_LIST);
    if (!r) r = consumeToken(b, TOKEN_SET);
    if (!r) r = consumeToken(b, TOKEN_MAP);
    if (!r) r = consumeToken(b, TOKEN_METADATA);
    if (!r) r = consumeToken(b, TOKEN_NAMESPACE);
    if (!r) r = consumeToken(b, TOKEN_STRUCTURE);
    if (!r) r = consumeToken(b, TOKEN_UNION);
    if (!r) r = consumeToken(b, TOKEN_SERVICE);
    if (!r) r = consumeToken(b, TOKEN_OPERATION);
    if (!r) r = consumeToken(b, TOKEN_RESOURCE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_LIST id shape_fields
  public static boolean list_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_definition")) return false;
    if (!nextTokenIs(b, TOKEN_LIST)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_LIST);
    r = r && id(b, l + 1);
    r = r && shape_fields(b, l + 1);
    exit_section_(b, m, LIST_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_MAP id shape_fields
  public static boolean map_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_definition")) return false;
    if (!nextTokenIs(b, TOKEN_MAP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_MAP);
    r = r && id(b, l + 1);
    r = r && shape_fields(b, l + 1);
    exit_section_(b, m, MAP_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_METADATA key TOKEN_EQUALS value
  public static boolean metadata_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_definition")) return false;
    if (!nextTokenIs(b, TOKEN_METADATA)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_METADATA);
    r = r && key(b, l + 1);
    r = r && consumeToken(b, TOKEN_EQUALS);
    r = r && value(b, l + 1);
    exit_section_(b, m, METADATA_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // metadata_definition*
  public static boolean metadata_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_section")) return false;
    Marker m = enter_section_(b, l, _NONE_, METADATA_SECTION, "<metadata section>");
    while (true) {
      int c = current_position_(b);
      if (!metadata_definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "metadata_section", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // id (TOKEN_PERIOD id)*
  public static boolean namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NAMESPACE, "<namespace>");
    r = id(b, l + 1);
    r = r && namespace_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (TOKEN_PERIOD id)*
  private static boolean namespace_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!namespace_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "namespace_1", c)) break;
    }
    return true;
  }

  // TOKEN_PERIOD id
  private static boolean namespace_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_PERIOD);
    r = r && id(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_NAMESPACE namespace
  public static boolean namespace_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_definition")) return false;
    if (!nextTokenIs(b, TOKEN_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_NAMESPACE);
    r = r && namespace(b, l + 1);
    exit_section_(b, m, NAMESPACE_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_NULL
  public static boolean null_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "null_$")) return false;
    if (!nextTokenIs(b, TOKEN_NULL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_NULL);
    exit_section_(b, m, NULL, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_NUMBER
  public static boolean number(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number")) return false;
    if (!nextTokenIs(b, TOKEN_NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_NUMBER);
    exit_section_(b, m, NUMBER, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_OPERATION id structure
  public static boolean operation_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operation_definition")) return false;
    if (!nextTokenIs(b, TOKEN_OPERATION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPERATION);
    r = r && id(b, l + 1);
    r = r && structure(b, l + 1);
    exit_section_(b, m, OPERATION_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // null | boolean | number | string | text_block | shape_id
  public static boolean primitive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE, "<primitive>");
    r = null_$(b, l + 1);
    if (!r) r = boolean_$(b, l + 1);
    if (!r) r = number(b, l + 1);
    if (!r) r = string(b, l + 1);
    if (!r) r = text_block(b, l + 1);
    if (!r) r = shape_id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_RESOURCE id structure
  public static boolean resource_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_definition")) return false;
    if (!nextTokenIs(b, TOKEN_RESOURCE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_RESOURCE);
    r = r && id(b, l + 1);
    r = r && structure(b, l + 1);
    exit_section_(b, m, RESOURCE_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // control_section metadata_section [shape_section]
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = control_section(b, l + 1);
    r = r && metadata_section(b, l + 1);
    r = r && root_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [shape_section]
  private static boolean root_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_2")) return false;
    shape_section(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [namespace TOKEN_HASH] id
  public static boolean root_shape_id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_shape_id")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ROOT_SHAPE_ID, "<root shape id>");
    r = root_shape_id_0(b, l + 1);
    r = r && id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [namespace TOKEN_HASH]
  private static boolean root_shape_id_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_shape_id_0")) return false;
    root_shape_id_0_0(b, l + 1);
    return true;
  }

  // namespace TOKEN_HASH
  private static boolean root_shape_id_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_shape_id_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespace(b, l + 1);
    r = r && consumeToken(b, TOKEN_HASH);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_SERVICE id structure
  public static boolean service_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service_definition")) return false;
    if (!nextTokenIs(b, TOKEN_SERVICE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_SERVICE);
    r = r && id(b, l + 1);
    r = r && structure(b, l + 1);
    exit_section_(b, m, SERVICE_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_SET id shape_fields
  public static boolean set_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_definition")) return false;
    if (!nextTokenIs(b, TOKEN_SET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_SET);
    r = r && id(b, l + 1);
    r = r && shape_fields(b, l + 1);
    exit_section_(b, m, SET_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // [documentation] traits (simple_shape_definition | list_definition | set_definition | map_definition | structure_definition | union_definition | service_definition | operation_definition | resource_definition)
  public static boolean shape_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_DEFINITION, "<shape definition>");
    r = shape_definition_0(b, l + 1);
    r = r && traits(b, l + 1);
    r = r && shape_definition_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean shape_definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_definition_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // simple_shape_definition | list_definition | set_definition | map_definition | structure_definition | union_definition | service_definition | operation_definition | resource_definition
  private static boolean shape_definition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_definition_2")) return false;
    boolean r;
    r = simple_shape_definition(b, l + 1);
    if (!r) r = list_definition(b, l + 1);
    if (!r) r = set_definition(b, l + 1);
    if (!r) r = map_definition(b, l + 1);
    if (!r) r = structure_definition(b, l + 1);
    if (!r) r = union_definition(b, l + 1);
    if (!r) r = service_definition(b, l + 1);
    if (!r) r = operation_definition(b, l + 1);
    if (!r) r = resource_definition(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // [documentation] traits id TOKEN_COLON shape_id
  public static boolean shape_field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_field")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_FIELD, "<shape field>");
    r = shape_field_0(b, l + 1);
    r = r && traits(b, l + 1);
    r = r && id(b, l + 1);
    r = r && consumeToken(b, TOKEN_COLON);
    r = r && shape_id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean shape_field_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_field_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_OPEN_BRACE [shape_field (TOKEN_COMMA shape_field)* [TOKEN_COMMA]] TOKEN_CLOSE_BRACE
  public static boolean shape_fields(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_fields")) return false;
    if (!nextTokenIs(b, TOKEN_OPEN_BRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPEN_BRACE);
    r = r && shape_fields_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_CLOSE_BRACE);
    exit_section_(b, m, SHAPE_FIELDS, r);
    return r;
  }

  // [shape_field (TOKEN_COMMA shape_field)* [TOKEN_COMMA]]
  private static boolean shape_fields_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_fields_1")) return false;
    shape_fields_1_0(b, l + 1);
    return true;
  }

  // shape_field (TOKEN_COMMA shape_field)* [TOKEN_COMMA]
  private static boolean shape_fields_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_fields_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = shape_field(b, l + 1);
    r = r && shape_fields_1_0_1(b, l + 1);
    r = r && shape_fields_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TOKEN_COMMA shape_field)*
  private static boolean shape_fields_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_fields_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!shape_fields_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shape_fields_1_0_1", c)) break;
    }
    return true;
  }

  // TOKEN_COMMA shape_field
  private static boolean shape_fields_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_fields_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_COMMA);
    r = r && shape_field(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_COMMA]
  private static boolean shape_fields_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_fields_1_0_2")) return false;
    consumeToken(b, TOKEN_COMMA);
    return true;
  }

  /* ********************************************************** */
  // root_shape_id [shape_id_member]
  public static boolean shape_id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_ID, "<shape id>");
    r = root_shape_id(b, l + 1);
    r = r && shape_id_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [shape_id_member]
  private static boolean shape_id_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id_1")) return false;
    shape_id_member(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_DOLLAR_SIGN id
  public static boolean shape_id_member(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id_member")) return false;
    if (!nextTokenIs(b, TOKEN_DOLLAR_SIGN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_DOLLAR_SIGN);
    r = r && id(b, l + 1);
    exit_section_(b, m, SHAPE_ID_MEMBER, r);
    return r;
  }

  /* ********************************************************** */
  // namespace_definition imports shape_statements
  public static boolean shape_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_section")) return false;
    if (!nextTokenIs(b, TOKEN_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespace_definition(b, l + 1);
    r = r && imports(b, l + 1);
    r = r && shape_statements(b, l + 1);
    exit_section_(b, m, SHAPE_SECTION, r);
    return r;
  }

  /* ********************************************************** */
  // apply | shape_definition
  public static boolean shape_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_STATEMENT, "<shape statement>");
    r = apply(b, l + 1);
    if (!r) r = shape_definition(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // shape_statement*
  public static boolean shape_statements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_statements")) return false;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_STATEMENTS, "<shape statements>");
    while (true) {
      int c = current_position_(b);
      if (!shape_statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shape_statements", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // simple_type_name id
  public static boolean simple_shape_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_shape_definition")) return false;
    if (!nextTokenIs(b, TOKEN_SIMPLE_TYPE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_type_name(b, l + 1);
    r = r && id(b, l + 1);
    exit_section_(b, m, SIMPLE_SHAPE_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_SIMPLE_TYPE_NAME
  public static boolean simple_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_type_name")) return false;
    if (!nextTokenIs(b, TOKEN_SIMPLE_TYPE_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_SIMPLE_TYPE_NAME);
    exit_section_(b, m, SIMPLE_TYPE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_STRING
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    if (!nextTokenIs(b, TOKEN_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_STRING);
    exit_section_(b, m, STRING, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_OPEN_BRACE [entry (TOKEN_COMMA entry)* [TOKEN_COMMA]] TOKEN_CLOSE_BRACE
  public static boolean structure(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure")) return false;
    if (!nextTokenIs(b, TOKEN_OPEN_BRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPEN_BRACE);
    r = r && structure_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_CLOSE_BRACE);
    exit_section_(b, m, STRUCTURE, r);
    return r;
  }

  // [entry (TOKEN_COMMA entry)* [TOKEN_COMMA]]
  private static boolean structure_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_1")) return false;
    structure_1_0(b, l + 1);
    return true;
  }

  // entry (TOKEN_COMMA entry)* [TOKEN_COMMA]
  private static boolean structure_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = entry(b, l + 1);
    r = r && structure_1_0_1(b, l + 1);
    r = r && structure_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TOKEN_COMMA entry)*
  private static boolean structure_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!structure_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "structure_1_0_1", c)) break;
    }
    return true;
  }

  // TOKEN_COMMA entry
  private static boolean structure_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_COMMA);
    r = r && entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_COMMA]
  private static boolean structure_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_1_0_2")) return false;
    consumeToken(b, TOKEN_COMMA);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_STRUCTURE id shape_fields
  public static boolean structure_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_definition")) return false;
    if (!nextTokenIs(b, TOKEN_STRUCTURE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_STRUCTURE);
    r = r && id(b, l + 1);
    r = r && shape_fields(b, l + 1);
    exit_section_(b, m, STRUCTURE_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_SYMBOL
  public static boolean symbol(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "symbol")) return false;
    if (!nextTokenIs(b, TOKEN_SYMBOL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_SYMBOL);
    exit_section_(b, m, SYMBOL, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_TEXT_BLOCK
  public static boolean text_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "text_block")) return false;
    if (!nextTokenIs(b, TOKEN_TEXT_BLOCK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_TEXT_BLOCK);
    exit_section_(b, m, TEXT_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // trait_name [trait_body]
  public static boolean trait(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait")) return false;
    if (!nextTokenIs(b, TOKEN_AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = trait_name(b, l + 1);
    r = r && trait_1(b, l + 1);
    exit_section_(b, m, TRAIT, r);
    return r;
  }

  // [trait_body]
  private static boolean trait_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_1")) return false;
    trait_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_OPEN_PAREN (trait_values | value) TOKEN_CLOSE_PAREN
  public static boolean trait_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_body")) return false;
    if (!nextTokenIs(b, TOKEN_OPEN_PAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPEN_PAREN);
    r = r && trait_body_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_CLOSE_PAREN);
    exit_section_(b, m, TRAIT_BODY, r);
    return r;
  }

  // trait_values | value
  private static boolean trait_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_body_1")) return false;
    boolean r;
    r = trait_values(b, l + 1);
    if (!r) r = value(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_AT shape_id
  public static boolean trait_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_name")) return false;
    if (!nextTokenIs(b, TOKEN_AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_AT);
    r = r && shape_id(b, l + 1);
    exit_section_(b, m, TRAIT_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // entry (TOKEN_COMMA entry)* [TOKEN_COMMA]
  public static boolean trait_values(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_values")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TRAIT_VALUES, "<trait values>");
    r = entry(b, l + 1);
    r = r && trait_values_1(b, l + 1);
    r = r && trait_values_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (TOKEN_COMMA entry)*
  private static boolean trait_values_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_values_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait_values_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "trait_values_1", c)) break;
    }
    return true;
  }

  // TOKEN_COMMA entry
  private static boolean trait_values_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_values_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_COMMA);
    r = r && entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_COMMA]
  private static boolean trait_values_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_values_2")) return false;
    consumeToken(b, TOKEN_COMMA);
    return true;
  }

  /* ********************************************************** */
  // trait*
  public static boolean traits(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "traits")) return false;
    Marker m = enter_section_(b, l, _NONE_, TRAITS, "<traits>");
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "traits", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_UNION id shape_fields
  public static boolean union_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "union_definition")) return false;
    if (!nextTokenIs(b, TOKEN_UNION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_UNION);
    r = r && id(b, l + 1);
    r = r && shape_fields(b, l + 1);
    exit_section_(b, m, UNION_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // array | structure | primitive
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = array(b, l + 1);
    if (!r) r = structure(b, l + 1);
    if (!r) r = primitive(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
