package software.amazon.smithy.intellij.selector;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static software.amazon.smithy.intellij.selector.psi.SmithySelectorTypes.*;

%%

%{
  public _SmithySelectorLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SmithySelectorLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

TOKEN_STRING=('[^']*')|(\"[^\"]*\")
TOKEN_SIMPLE_TYPE_NAME=blob|boolean|document|string|byte|short|integer|long|float|double|bigInteger|bigDecimal|timestamp
TOKEN_SYMBOL=_*[A-Za-z][A-Za-z0-9_]*
TOKEN_NUMBER=-?(0|([1-9][0-9]*))(\.[0-9]+)?(e[+-]?[0-9]+)?

%%
<YYINITIAL> {
  {WHITE_SPACE}                 { return WHITE_SPACE; }

  "~>"                          { return TOKEN_FORWARD_RECURSIVE_NEIGHBOR; }
  "-["                          { return TOKEN_FORWARD_DIRECTED_NEIGHBOR_START; }
  "]->"                         { return TOKEN_FORWARD_DIRECTED_NEIGHBOR_END; }
  "<-["                         { return TOKEN_REVERSE_DIRECTED_NEIGHBOR_START; }
  "]-"                          { return TOKEN_REVERSE_DIRECTED_NEIGHBOR_END; }
  "="                           { return TOKEN_EQUALS; }
  "!="                          { return TOKEN_NOT_EQUALS; }
  ">"                           { return TOKEN_GREATER_THAN; }
  ">="                          { return TOKEN_GREATER_THAN_EQUALS; }
  "<"                           { return TOKEN_LESS_THAN; }
  "<="                          { return TOKEN_LESS_THAN_EQUALS; }
  "^="                          { return TOKEN_STARTS_WITH; }
  "$="                          { return TOKEN_ENDS_WITH; }
  "*="                          { return TOKEN_CONTAINS; }
  "?="                          { return TOKEN_EXISTS; }
  "{=}"                         { return TOKEN_PROJECTION_EQUALS; }
  "{!=}"                        { return TOKEN_PROJECTION_NOT_EQUALS; }
  "{<}"                         { return TOKEN_SUBSET; }
  "{<<}"                        { return TOKEN_PROPER_SUBSET; }
  "[@"                          { return TOKEN_SCOPED_ATTRIBUTE_START; }
  "&&"                          { return TOKEN_AND; }
  "@{"                          { return TOKEN_CONTEXT_VALUE_START; }
  "${"                          { return TOKEN_VARIABLE_GET_START; }
  "|"                           { return TOKEN_PIPE; }
  "i"                           { return TOKEN_INVERSE; }
  "("                           { return TOKEN_OPEN_PAREN; }
  ")"                           { return TOKEN_CLOSE_PAREN; }
  "}"                           { return TOKEN_CLOSE_BRACE; }
  "["                           { return TOKEN_OPEN_BRACKET; }
  "]"                           { return TOKEN_CLOSE_BRACKET; }
  ":"                           { return TOKEN_COLON; }
  ","                           { return TOKEN_COMMA; }
  "."                           { return TOKEN_PERIOD; }
  "$"                           { return TOKEN_DOLLAR_SIGN; }
  "#"                           { return TOKEN_HASH; }
  "*"                           { return TOKEN_ASTERISK; }
  "collection"                  { return TOKEN_COLLECTION_TYPE; }
  "member"                      { return TOKEN_MEMBER_TYPE; }
  "number"                      { return TOKEN_NUMBER_TYPE; }
  "simpleType"                  { return TOKEN_SIMPLE_TYPE; }
  "list"                        { return TOKEN_LIST; }
  "set"                         { return TOKEN_SET; }
  "map"                         { return TOKEN_MAP; }
  "structure"                   { return TOKEN_STRUCTURE; }
  "union"                       { return TOKEN_UNION; }
  "service"                     { return TOKEN_SERVICE; }
  "operation"                   { return TOKEN_OPERATION; }
  "resource"                    { return TOKEN_RESOURCE; }

  {TOKEN_STRING}                { return TOKEN_STRING; }
  {TOKEN_SIMPLE_TYPE_NAME}      { return TOKEN_SIMPLE_TYPE_NAME; }
  {TOKEN_SYMBOL}                { return TOKEN_SYMBOL; }
  {TOKEN_NUMBER}                { return TOKEN_NUMBER; }

}

[^] { return BAD_CHARACTER; }
