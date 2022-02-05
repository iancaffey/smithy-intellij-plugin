package software.amazon.smithy.intellij;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;

%%

%{
  public _SmithyLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SmithyLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

TOKEN_BOOLEAN=true|false
TOKEN_SIMPLE_TYPE_NAME=blob|boolean|document|string|byte|short|integer|long|float|double|bigInteger|bigDecimal|timestamp
TOKEN_SYMBOL=_*[A-Za-z][A-Za-z0-9_]*
TOKEN_NUMBER=-?(0|([1-9][0-9]*))(\.[0-9]+)?(e[+-]?[0-9]+)?
TOKEN_STRING=\"((\\['bfnrt/\"\\])|(\\u[0-9A-Fa-f]{4})|(\\?[ !#-\[\]-\U10FFFF]))*\"
TOKEN_TEXT_BLOCK=\"\"\"\n((\\['bfnrt/\"\\])|(\\u[0-9A-Fa-f]{4})|(\\?[ !#-\[\]-\U10FFFF])|(\\[\"]{3})|(\\?\r?\n))*\"\"\"
TOKEN_COMMENT="//"[\t -\U10FFFF]*\r?\n
TOKEN_INCOMPLETE_STRING=\"((\\['bfnrt/\"\\])|(\\u[0-9A-Fa-f]{4})|(\\?[ !#-\[\]-\U10FFFF]))+
TOKEN_INCOMPLETE_TEXT_BLOCK=\"\"\"\n((\\['bfnrt/\"\\])|(\\u[0-9A-Fa-f]{4})|(\\?[ !#-\[\]-\U10FFFF])|(\\[\"]{3})|(\\?\r?\n))+

%%
<YYINITIAL> {
  {WHITE_SPACE}                      { return WHITE_SPACE; }

  "("                                { return TOKEN_OPEN_PAREN; }
  ")"                                { return TOKEN_CLOSE_PAREN; }
  "{"                                { return TOKEN_OPEN_BRACE; }
  "}"                                { return TOKEN_CLOSE_BRACE; }
  "["                                { return TOKEN_OPEN_BRACKET; }
  "]"                                { return TOKEN_CLOSE_BRACKET; }
  ":"                                { return TOKEN_COLON; }
  ","                                { return TOKEN_COMMA; }
  "="                                { return TOKEN_EQUALS; }
  "."                                { return TOKEN_PERIOD; }
  "$"                                { return TOKEN_DOLLAR_SIGN; }
  "@"                                { return TOKEN_AT; }
  "#"                                { return TOKEN_HASH; }
  "apply"                            { return TOKEN_APPLY; }
  "use"                              { return TOKEN_USE; }
  "list"                             { return TOKEN_LIST; }
  "set"                              { return TOKEN_SET; }
  "map"                              { return TOKEN_MAP; }
  "metadata"                         { return TOKEN_METADATA; }
  "namespace"                        { return TOKEN_NAMESPACE; }
  "structure"                        { return TOKEN_STRUCTURE; }
  "union"                            { return TOKEN_UNION; }
  "service"                          { return TOKEN_SERVICE; }
  "operation"                        { return TOKEN_OPERATION; }
  "resource"                         { return TOKEN_RESOURCE; }
  "null"                             { return TOKEN_NULL; }

  {TOKEN_BOOLEAN}                    { return TOKEN_BOOLEAN; }
  {TOKEN_SIMPLE_TYPE_NAME}           { return TOKEN_SIMPLE_TYPE_NAME; }
  {TOKEN_SYMBOL}                     { return TOKEN_SYMBOL; }
  {TOKEN_NUMBER}                     { return TOKEN_NUMBER; }
  {TOKEN_STRING}                     { return TOKEN_STRING; }
  {TOKEN_TEXT_BLOCK}                 { return TOKEN_TEXT_BLOCK; }
  {TOKEN_COMMENT}                    { return TOKEN_COMMENT; }
  {TOKEN_INCOMPLETE_STRING}          { return TOKEN_INCOMPLETE_STRING; }
  {TOKEN_INCOMPLETE_TEXT_BLOCK}      { return TOKEN_INCOMPLETE_TEXT_BLOCK; }

}

[^] { return BAD_CHARACTER; }
