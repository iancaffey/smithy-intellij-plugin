// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
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
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
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

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(CONTROL, ENTRY, METADATA),
    create_token_set_(ID, KEYWORD, SIMPLE_TYPE_NAME, SYMBOL),
    create_token_set_(LIST, MAP, OPERATION, RESOURCE,
      SERVICE, SET, SHAPE, SIMPLE_SHAPE,
      STRUCTURE, UNION),
    create_token_set_(ARRAY, BOOLEAN, NULL, NUMBER,
      OBJECT, PRIMITIVE, SHAPE_ID, STRING,
      TEXT_BLOCK, VALUE),
  };

  /* ********************************************************** */
  // TOKEN_APPLY shape_id trait
  public static boolean applied_trait(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "applied_trait")) return false;
    if (!nextTokenIs(b, TOKEN_APPLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_APPLY);
    r = r && shape_id(b, l + 1);
    r = r && trait(b, l + 1);
    exit_section_(b, m, APPLIED_TRAIT, r);
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
  public static boolean control(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control")) return false;
    if (!nextTokenIs(b, TOKEN_DOLLAR_SIGN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_DOLLAR_SIGN);
    r = r && key(b, l + 1);
    r = r && consumeToken(b, TOKEN_COLON);
    r = r && value(b, l + 1);
    exit_section_(b, m, CONTROL, r);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_DOCUMENTATION_LINE+
  public static boolean documentation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "documentation")) return false;
    if (!nextTokenIs(b, TOKEN_DOCUMENTATION_LINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_DOCUMENTATION_LINE);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, TOKEN_DOCUMENTATION_LINE)) break;
      if (!empty_element_parsed_guard_(b, "documentation", c)) break;
    }
    exit_section_(b, m, DOCUMENTATION, r);
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
    Marker m = enter_section_(b, l, _COLLAPSE_, ID, "<id>");
    r = symbol(b, l + 1);
    if (!r) r = keyword(b, l + 1);
    if (!r) r = null_$(b, l + 1);
    if (!r) r = boolean_$(b, l + 1);
    if (!r) r = simple_type_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_USE shape_id
  public static boolean import_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_$")) return false;
    if (!nextTokenIs(b, TOKEN_USE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_USE);
    r = r && shape_id(b, l + 1);
    exit_section_(b, m, IMPORT, r);
    return r;
  }

  /* ********************************************************** */
  // key [TOKEN_COLON]
  public static boolean incomplete_entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incomplete_entry")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INCOMPLETE_ENTRY, "<incomplete entry>");
    r = key(b, l + 1);
    r = r && incomplete_entry_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [TOKEN_COLON]
  private static boolean incomplete_entry_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incomplete_entry_1")) return false;
    consumeToken(b, TOKEN_COLON);
    return true;
  }

  /* ********************************************************** */
  // member_name [TOKEN_COLON]
  public static boolean incomplete_member(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incomplete_member")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INCOMPLETE_MEMBER, "<incomplete member>");
    r = member_name(b, l + 1);
    r = r && incomplete_member_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [TOKEN_COLON]
  private static boolean incomplete_member_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incomplete_member_1")) return false;
    consumeToken(b, TOKEN_COLON);
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
  // [documentation] trait* TOKEN_LIST shape_name shape_body
  public static boolean list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LIST, "<list>");
    r = list_0(b, l + 1);
    r = r && list_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_LIST);
    r = r && shape_name(b, l + 1);
    r = r && shape_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "list_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // [documentation] trait* TOKEN_MAP shape_name shape_body
  public static boolean map(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP, "<map>");
    r = map_0(b, l + 1);
    r = r && map_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_MAP);
    r = r && shape_name(b, l + 1);
    r = r && shape_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean map_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean map_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "map_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // [documentation] trait* member_name TOKEN_COLON shape_id
  public static boolean member(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MEMBER, "<member>");
    r = member_0(b, l + 1);
    r = r && member_1(b, l + 1);
    r = r && member_name(b, l + 1);
    r = r && consumeToken(b, TOKEN_COLON);
    r = r && shape_id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean member_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean member_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "member_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // id
  public static boolean member_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MEMBER_NAME, "<member name>");
    r = id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_METADATA key TOKEN_EQUALS value
  public static boolean metadata(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata")) return false;
    if (!nextTokenIs(b, TOKEN_METADATA)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_METADATA);
    r = r && key(b, l + 1);
    r = r && consumeToken(b, TOKEN_EQUALS);
    r = r && value(b, l + 1);
    exit_section_(b, m, METADATA, r);
    return r;
  }

  /* ********************************************************** */
  // control* metadata* [namespace import* (applied_trait | shape)*]
  public static boolean model(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODEL, "<model>");
    r = model_0(b, l + 1);
    r = r && model_1(b, l + 1);
    r = r && model_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // control*
  private static boolean model_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!control(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "model_0", c)) break;
    }
    return true;
  }

  // metadata*
  private static boolean model_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "model_1", c)) break;
    }
    return true;
  }

  // [namespace import* (applied_trait | shape)*]
  private static boolean model_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_2")) return false;
    model_2_0(b, l + 1);
    return true;
  }

  // namespace import* (applied_trait | shape)*
  private static boolean model_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespace(b, l + 1);
    r = r && model_2_0_1(b, l + 1);
    r = r && model_2_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // import*
  private static boolean model_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!import_$(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "model_2_0_1", c)) break;
    }
    return true;
  }

  // (applied_trait | shape)*
  private static boolean model_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_2_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!model_2_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "model_2_0_2", c)) break;
    }
    return true;
  }

  // applied_trait | shape
  private static boolean model_2_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "model_2_0_2_0")) return false;
    boolean r;
    r = applied_trait(b, l + 1);
    if (!r) r = shape(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_NAMESPACE namespace_id
  public static boolean namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace")) return false;
    if (!nextTokenIs(b, TOKEN_NAMESPACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_NAMESPACE);
    r = r && namespace_id(b, l + 1);
    exit_section_(b, m, NAMESPACE, r);
    return r;
  }

  /* ********************************************************** */
  // id (TOKEN_PERIOD id)*
  public static boolean namespace_id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_id")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NAMESPACE_ID, "<namespace id>");
    r = id(b, l + 1);
    r = r && namespace_id_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (TOKEN_PERIOD id)*
  private static boolean namespace_id_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_id_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!namespace_id_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "namespace_id_1", c)) break;
    }
    return true;
  }

  // TOKEN_PERIOD id
  private static boolean namespace_id_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace_id_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_PERIOD);
    r = r && id(b, l + 1);
    exit_section_(b, m, null, r);
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
  // TOKEN_OPEN_BRACE [entry (TOKEN_COMMA entry)* [TOKEN_COMMA] [incomplete_entry]] TOKEN_CLOSE_BRACE
  public static boolean object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object")) return false;
    if (!nextTokenIs(b, TOKEN_OPEN_BRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPEN_BRACE);
    r = r && object_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_CLOSE_BRACE);
    exit_section_(b, m, OBJECT, r);
    return r;
  }

  // [entry (TOKEN_COMMA entry)* [TOKEN_COMMA] [incomplete_entry]]
  private static boolean object_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1")) return false;
    object_1_0(b, l + 1);
    return true;
  }

  // entry (TOKEN_COMMA entry)* [TOKEN_COMMA] [incomplete_entry]
  private static boolean object_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = entry(b, l + 1);
    r = r && object_1_0_1(b, l + 1);
    r = r && object_1_0_2(b, l + 1);
    r = r && object_1_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TOKEN_COMMA entry)*
  private static boolean object_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!object_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "object_1_0_1", c)) break;
    }
    return true;
  }

  // TOKEN_COMMA entry
  private static boolean object_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_COMMA);
    r = r && entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_COMMA]
  private static boolean object_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_2")) return false;
    consumeToken(b, TOKEN_COMMA);
    return true;
  }

  // [incomplete_entry]
  private static boolean object_1_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_3")) return false;
    incomplete_entry(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [documentation] trait* TOKEN_OPERATION shape_name object
  public static boolean operation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operation")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATION, "<operation>");
    r = operation_0(b, l + 1);
    r = r && operation_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_OPERATION);
    r = r && shape_name(b, l + 1);
    r = r && object(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean operation_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operation_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean operation_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operation_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "operation_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // null | boolean | number | string | text_block | shape_id
  public static boolean primitive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, PRIMITIVE, "<primitive>");
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
  // [documentation] trait* TOKEN_RESOURCE shape_name object
  public static boolean resource(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESOURCE, "<resource>");
    r = resource_0(b, l + 1);
    r = r && resource_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_RESOURCE);
    r = r && shape_name(b, l + 1);
    r = r && object(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean resource_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean resource_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "resource_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // model
  static boolean root(PsiBuilder b, int l) {
    return model(b, l + 1);
  }

  /* ********************************************************** */
  // [documentation] trait* TOKEN_SERVICE shape_name object
  public static boolean service(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SERVICE, "<service>");
    r = service_0(b, l + 1);
    r = r && service_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_SERVICE);
    r = r && shape_name(b, l + 1);
    r = r && object(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean service_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean service_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "service_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "service_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // [documentation] trait* TOKEN_SET shape_name shape_body
  public static boolean set(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET, "<set>");
    r = set_0(b, l + 1);
    r = r && set_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_SET);
    r = r && shape_name(b, l + 1);
    r = r && shape_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean set_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean set_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "set_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // simple_shape | list | set | map | structure | union | service | operation | resource
  public static boolean shape(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, SHAPE, "<shape>");
    r = simple_shape(b, l + 1);
    if (!r) r = list(b, l + 1);
    if (!r) r = set(b, l + 1);
    if (!r) r = map(b, l + 1);
    if (!r) r = structure(b, l + 1);
    if (!r) r = union(b, l + 1);
    if (!r) r = service(b, l + 1);
    if (!r) r = operation(b, l + 1);
    if (!r) r = resource(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TOKEN_OPEN_BRACE [member (TOKEN_COMMA member)* [TOKEN_COMMA] [incomplete_member]] TOKEN_CLOSE_BRACE
  public static boolean shape_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body")) return false;
    if (!nextTokenIs(b, TOKEN_OPEN_BRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_OPEN_BRACE);
    r = r && shape_body_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_CLOSE_BRACE);
    exit_section_(b, m, SHAPE_BODY, r);
    return r;
  }

  // [member (TOKEN_COMMA member)* [TOKEN_COMMA] [incomplete_member]]
  private static boolean shape_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body_1")) return false;
    shape_body_1_0(b, l + 1);
    return true;
  }

  // member (TOKEN_COMMA member)* [TOKEN_COMMA] [incomplete_member]
  private static boolean shape_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = member(b, l + 1);
    r = r && shape_body_1_0_1(b, l + 1);
    r = r && shape_body_1_0_2(b, l + 1);
    r = r && shape_body_1_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (TOKEN_COMMA member)*
  private static boolean shape_body_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!shape_body_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shape_body_1_0_1", c)) break;
    }
    return true;
  }

  // TOKEN_COMMA member
  private static boolean shape_body_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_COMMA);
    r = r && member(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_COMMA]
  private static boolean shape_body_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body_1_0_2")) return false;
    consumeToken(b, TOKEN_COMMA);
    return true;
  }

  // [incomplete_member]
  private static boolean shape_body_1_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_body_1_0_3")) return false;
    incomplete_member(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [namespace_id TOKEN_HASH] id [TOKEN_DOLLAR_SIGN member_name]
  public static boolean shape_id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_ID, "<shape id>");
    r = shape_id_0(b, l + 1);
    r = r && id(b, l + 1);
    r = r && shape_id_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [namespace_id TOKEN_HASH]
  private static boolean shape_id_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id_0")) return false;
    shape_id_0_0(b, l + 1);
    return true;
  }

  // namespace_id TOKEN_HASH
  private static boolean shape_id_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespace_id(b, l + 1);
    r = r && consumeToken(b, TOKEN_HASH);
    exit_section_(b, m, null, r);
    return r;
  }

  // [TOKEN_DOLLAR_SIGN member_name]
  private static boolean shape_id_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id_2")) return false;
    shape_id_2_0(b, l + 1);
    return true;
  }

  // TOKEN_DOLLAR_SIGN member_name
  private static boolean shape_id_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_id_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_DOLLAR_SIGN);
    r = r && member_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean shape_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shape_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_NAME, "<shape name>");
    r = id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [documentation] trait* simple_type_name shape_name
  public static boolean simple_shape(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_shape")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_SHAPE, "<simple shape>");
    r = simple_shape_0(b, l + 1);
    r = r && simple_shape_1(b, l + 1);
    r = r && simple_type_name(b, l + 1);
    r = r && shape_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean simple_shape_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_shape_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean simple_shape_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_shape_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simple_shape_1", c)) break;
    }
    return true;
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
  // [documentation] trait* TOKEN_STRUCTURE shape_name shape_body
  public static boolean structure(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCTURE, "<structure>");
    r = structure_0(b, l + 1);
    r = r && structure_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_STRUCTURE);
    r = r && shape_name(b, l + 1);
    r = r && shape_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean structure_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean structure_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "structure_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "structure_1", c)) break;
    }
    return true;
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
  // TOKEN_AT shape_id [trait_body]
  public static boolean trait(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait")) return false;
    if (!nextTokenIs(b, TOKEN_AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TOKEN_AT);
    r = r && shape_id(b, l + 1);
    r = r && trait_2(b, l + 1);
    exit_section_(b, m, TRAIT, r);
    return r;
  }

  // [trait_body]
  private static boolean trait_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_2")) return false;
    trait_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TOKEN_OPEN_PAREN [trait_values | value] TOKEN_CLOSE_PAREN
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

  // [trait_values | value]
  private static boolean trait_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_body_1")) return false;
    trait_body_1_0(b, l + 1);
    return true;
  }

  // trait_values | value
  private static boolean trait_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_body_1_0")) return false;
    boolean r;
    r = trait_values(b, l + 1);
    if (!r) r = value(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // entry (TOKEN_COMMA entry)* [TOKEN_COMMA] [incomplete_entry]
  static boolean trait_values(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_values")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = entry(b, l + 1);
    r = r && trait_values_1(b, l + 1);
    r = r && trait_values_2(b, l + 1);
    r = r && trait_values_3(b, l + 1);
    exit_section_(b, m, null, r);
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

  // [incomplete_entry]
  private static boolean trait_values_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trait_values_3")) return false;
    incomplete_entry(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [documentation] trait* TOKEN_UNION shape_name shape_body
  public static boolean union(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "union")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNION, "<union>");
    r = union_0(b, l + 1);
    r = r && union_1(b, l + 1);
    r = r && consumeToken(b, TOKEN_UNION);
    r = r && shape_name(b, l + 1);
    r = r && shape_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [documentation]
  private static boolean union_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "union_0")) return false;
    documentation(b, l + 1);
    return true;
  }

  // trait*
  private static boolean union_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "union_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!trait(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "union_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // array | object | primitive
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, VALUE, "<value>");
    r = array(b, l + 1);
    if (!r) r = object(b, l + 1);
    if (!r) r = primitive(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
