/* The following code was generated by JFlex 1.7.0 tweaked for IntelliJ platform */

package software.amazon.smithy.intellij;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static software.amazon.smithy.intellij.psi.SmithyTypes.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>Smithy.flex</tt>
 */
public class _SmithyLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [7, 7, 7]
   * Total runtime size is 1928 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[ZZ_CMAP_Z[ch>>14]|((ch>>7)&0x7f)]<<7)|(ch&0x7f)];
  }

  /* The ZZ_CMAP_Z table has 68 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\103\200");

  /* The ZZ_CMAP_Y table has 256 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\53\2\1\3\22\2\1\4\37\2\1\3\237\2");

  /* The ZZ_CMAP_A table has 640 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\4\1\2\2\1\1\3\22\0\1\44\1\43\1\41\1\61\1\57\3\43\1\46\1\47\1\43\1"+
    "\40\1\55\1\35\1\37\1\45\1\36\11\34\1\54\2\43\1\56\2\43\1\60\3\33\1\30\4\33"+
    "\1\27\21\33\1\52\1\42\1\53\1\43\1\32\1\43\1\12\1\15\1\21\1\20\1\10\1\11\1"+
    "\24\1\26\1\23\2\33\1\13\1\22\1\17\1\16\1\31\1\33\1\6\1\14\1\5\1\7\1\62\2\33"+
    "\1\25\1\33\1\50\1\43\1\51\7\43\1\44\32\43\1\44\337\43\1\44\177\43\13\44\35"+
    "\43\2\44\5\43\1\44\57\43\1\44\40\43");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\16\3\1\1\1\4\1\1\1\4"+
    "\1\5\2\1\1\6\1\7\1\10\1\11\1\12\1\13"+
    "\1\14\1\15\1\16\1\17\1\20\1\21\30\3\3\0"+
    "\1\22\1\23\1\0\1\24\3\3\1\25\7\3\1\26"+
    "\14\3\1\27\1\3\1\4\1\0\1\4\1\23\1\0"+
    "\1\24\1\30\1\31\5\3\1\32\1\33\5\3\1\34"+
    "\5\3\1\0\2\3\1\35\1\36\11\3\1\37\3\0"+
    "\13\3\1\0\1\37\3\3\1\40\4\3\1\41\1\0"+
    "\1\3\1\42\4\3\1\43\1\37\1\44\1\3\1\45"+
    "\1\46\1\41";

  private static int [] zzUnpackAction() {
    int [] result = new int[173];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\63\0\146\0\231\0\314\0\377\0\u0132\0\u0165"+
    "\0\u0198\0\u01cb\0\u01fe\0\u0231\0\u0264\0\u0297\0\u02ca\0\u02fd"+
    "\0\u0330\0\u0363\0\u0396\0\u03c9\0\u03fc\0\63\0\u042f\0\u0462"+
    "\0\63\0\63\0\63\0\63\0\63\0\63\0\63\0\63"+
    "\0\63\0\63\0\63\0\63\0\u0495\0\u04c8\0\u04fb\0\u052e"+
    "\0\u0561\0\u0594\0\u05c7\0\u05fa\0\u062d\0\u0660\0\u0693\0\u06c6"+
    "\0\u06f9\0\u072c\0\u075f\0\u0792\0\u07c5\0\u07f8\0\u082b\0\u085e"+
    "\0\u0891\0\u08c4\0\u08f7\0\u092a\0\u0363\0\u095d\0\u0990\0\u09c3"+
    "\0\u09f6\0\u0a29\0\u0a5c\0\u0a8f\0\u0ac2\0\u0af5\0\u0132\0\u0b28"+
    "\0\u0b5b\0\u0b8e\0\u0bc1\0\u0bf4\0\u0c27\0\u0c5a\0\u0132\0\u0c8d"+
    "\0\u0cc0\0\u0cf3\0\u0d26\0\u0d59\0\u0d8c\0\u0dbf\0\u0df2\0\u0e25"+
    "\0\u0e58\0\u0e8b\0\u0ebe\0\u0132\0\u0ef1\0\u0f24\0\u0f24\0\u0f57"+
    "\0\63\0\u0f8a\0\u0fbd\0\u0ff0\0\u0132\0\u1023\0\u1056\0\u1089"+
    "\0\u10bc\0\u10ef\0\u0132\0\u0132\0\u1122\0\u1155\0\u1188\0\u11bb"+
    "\0\u11ee\0\u0132\0\u1221\0\u1254\0\u1287\0\u12ba\0\u12ed\0\u1320"+
    "\0\u1353\0\u1386\0\u0132\0\u0132\0\u13b9\0\u13ec\0\u141f\0\u1452"+
    "\0\u1485\0\u14b8\0\u14eb\0\u151e\0\u1551\0\u1320\0\u1584\0\u15b7"+
    "\0\u15ea\0\u161d\0\u1650\0\u1683\0\u16b6\0\u16e9\0\u171c\0\u174f"+
    "\0\u1782\0\u17b5\0\u17e8\0\u181b\0\u184e\0\u1881\0\u18b4\0\u18e7"+
    "\0\u191a\0\u0132\0\u194d\0\u1980\0\u19b3\0\u19e6\0\63\0\u1a19"+
    "\0\u1a4c\0\u0132\0\u1a7f\0\u1ab2\0\u1ae5\0\u1b18\0\u0132\0\u1b4b"+
    "\0\u0132\0\u1b7e\0\u0132\0\u0132\0\u15b7";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[173];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\4\3\1\4\1\5\1\6\1\7\1\10\1\11"+
    "\1\12\1\13\1\14\1\15\1\16\1\17\1\7\1\20"+
    "\1\21\6\7\1\22\1\7\1\23\1\24\1\25\1\26"+
    "\1\2\1\27\2\2\1\3\1\30\1\31\1\32\1\33"+
    "\1\34\1\35\1\36\1\37\1\40\1\41\1\42\1\43"+
    "\1\44\1\7\64\0\4\3\37\0\1\3\23\0\1\7"+
    "\1\45\14\7\1\46\11\7\1\0\1\7\23\0\1\7"+
    "\5\0\3\7\1\47\24\7\1\0\1\7\23\0\1\7"+
    "\5\0\7\7\1\50\2\7\1\51\15\7\1\0\1\7"+
    "\23\0\1\7\5\0\30\7\1\0\1\7\23\0\1\7"+
    "\5\0\5\7\1\52\1\53\21\7\1\0\1\7\23\0"+
    "\1\7\5\0\24\7\1\54\3\7\1\0\1\7\23\0"+
    "\1\7\5\0\11\7\1\55\4\7\1\56\11\7\1\0"+
    "\1\7\23\0\1\7\5\0\1\57\2\7\1\60\15\7"+
    "\1\61\6\7\1\0\1\7\23\0\1\7\5\0\6\7"+
    "\1\62\2\7\1\63\4\7\1\64\1\7\1\65\7\7"+
    "\1\0\1\7\23\0\1\7\5\0\24\7\1\66\3\7"+
    "\1\0\1\7\23\0\1\7\5\0\2\7\1\67\2\7"+
    "\1\70\22\7\1\0\1\7\23\0\1\7\5\0\11\7"+
    "\1\71\16\7\1\0\1\7\23\0\1\7\5\0\3\7"+
    "\1\72\1\7\1\73\22\7\1\0\1\7\23\0\1\7"+
    "\5\0\12\7\1\74\15\7\1\0\1\7\23\0\1\7"+
    "\5\0\25\7\1\75\1\7\26\0\1\7\10\0\1\76"+
    "\23\0\1\23\1\0\1\23\1\77\57\0\1\23\1\0"+
    "\1\25\34\0\1\76\26\0\1\77\30\0\34\100\1\101"+
    "\1\102\20\100\45\0\1\103\22\0\2\7\1\104\25\7"+
    "\1\0\1\7\23\0\1\7\5\0\15\7\1\105\12\7"+
    "\1\0\1\7\23\0\1\7\5\0\7\7\1\106\20\7"+
    "\1\0\1\7\23\0\1\7\5\0\3\7\1\107\24\7"+
    "\1\0\1\7\23\0\1\7\5\0\16\7\1\110\11\7"+
    "\1\0\1\7\23\0\1\7\5\0\6\7\1\111\21\7"+
    "\1\0\1\7\23\0\1\7\5\0\11\7\1\112\16\7"+
    "\1\0\1\7\23\0\1\7\5\0\24\7\1\113\3\7"+
    "\1\0\1\7\23\0\1\7\5\0\12\7\1\114\15\7"+
    "\1\0\1\7\23\0\1\7\5\0\7\7\1\115\20\7"+
    "\1\0\1\7\23\0\1\7\5\0\1\7\1\116\26\7"+
    "\1\0\1\7\23\0\1\7\5\0\1\117\1\120\26\7"+
    "\1\0\1\7\23\0\1\7\5\0\11\7\1\121\16\7"+
    "\1\0\1\7\23\0\1\7\5\0\11\7\1\122\16\7"+
    "\1\0\1\7\23\0\1\7\5\0\11\7\1\123\16\7"+
    "\1\0\1\7\23\0\1\7\5\0\17\7\1\124\10\7"+
    "\1\0\1\7\23\0\1\7\5\0\1\125\27\7\1\0"+
    "\1\7\23\0\1\7\5\0\3\7\1\126\24\7\1\0"+
    "\1\7\23\0\1\7\5\0\6\7\1\127\21\7\1\0"+
    "\1\7\23\0\1\7\5\0\15\7\1\130\12\7\1\0"+
    "\1\7\23\0\1\7\5\0\2\7\1\131\11\7\1\132"+
    "\13\7\1\0\1\7\23\0\1\7\5\0\1\133\27\7"+
    "\1\0\1\7\23\0\1\7\5\0\24\7\1\134\3\7"+
    "\1\0\1\7\23\0\1\7\5\0\1\135\27\7\1\0"+
    "\1\7\23\0\1\7\34\0\1\136\1\137\1\136\1\0"+
    "\1\137\56\0\1\140\1\0\1\140\31\0\34\100\1\141"+
    "\1\102\20\100\41\0\1\142\26\0\56\100\2\143\1\0"+
    "\42\143\1\144\15\143\5\0\3\7\1\145\24\7\1\0"+
    "\1\7\23\0\1\7\5\0\3\7\1\146\24\7\1\0"+
    "\1\7\23\0\1\7\5\0\11\7\1\147\16\7\1\0"+
    "\1\7\23\0\1\7\5\0\11\7\1\150\16\7\1\0"+
    "\1\7\23\0\1\7\5\0\7\7\1\104\20\7\1\0"+
    "\1\7\23\0\1\7\5\0\5\7\1\151\22\7\1\0"+
    "\1\7\23\0\1\7\5\0\6\7\1\152\21\7\1\0"+
    "\1\7\23\0\1\7\5\0\17\7\1\153\10\7\1\0"+
    "\1\7\23\0\1\7\5\0\1\154\27\7\1\0\1\7"+
    "\23\0\1\7\5\0\2\7\1\155\13\7\1\55\11\7"+
    "\1\0\1\7\23\0\1\7\5\0\30\7\1\0\1\7"+
    "\23\0\1\156\5\0\1\7\1\151\26\7\1\0\1\7"+
    "\23\0\1\7\5\0\10\7\1\153\17\7\1\0\1\7"+
    "\23\0\1\7\5\0\6\7\1\157\21\7\1\0\1\7"+
    "\23\0\1\7\5\0\22\7\1\21\1\160\4\7\1\0"+
    "\1\7\23\0\1\7\5\0\3\7\1\153\24\7\1\0"+
    "\1\7\23\0\1\7\5\0\1\7\1\161\26\7\1\0"+
    "\1\7\23\0\1\7\5\0\6\7\1\162\21\7\1\0"+
    "\1\7\23\0\1\7\5\0\3\7\1\163\24\7\1\0"+
    "\1\7\23\0\1\7\5\0\10\7\1\164\17\7\1\0"+
    "\1\7\23\0\1\7\5\0\2\7\1\165\25\7\1\0"+
    "\1\7\23\0\1\7\5\0\5\7\1\166\22\7\1\0"+
    "\1\7\23\0\1\7\5\0\3\7\1\167\24\7\1\0"+
    "\1\7\23\0\1\7\34\0\1\136\1\0\1\136\34\0"+
    "\1\76\23\0\1\140\1\0\1\140\26\0\1\170\64\0"+
    "\57\143\4\0\57\144\5\0\7\7\1\171\20\7\1\0"+
    "\1\7\23\0\1\7\5\0\2\7\1\172\25\7\1\0"+
    "\1\7\23\0\1\7\5\0\12\7\1\173\15\7\1\0"+
    "\1\7\23\0\1\7\5\0\1\153\27\7\1\0\1\7"+
    "\23\0\1\7\5\0\20\7\1\174\7\7\1\0\1\7"+
    "\23\0\1\7\5\0\14\7\1\175\13\7\1\0\1\7"+
    "\23\0\1\7\5\0\16\7\1\176\11\7\1\0\1\7"+
    "\23\0\1\7\5\0\3\7\1\177\24\7\1\0\1\7"+
    "\23\0\1\7\5\0\3\7\1\200\24\7\1\0\1\7"+
    "\23\0\1\7\5\0\5\7\1\201\22\7\1\0\1\7"+
    "\23\0\1\7\5\0\7\7\1\202\20\7\1\0\1\7"+
    "\23\0\1\7\5\0\6\7\1\125\21\7\1\0\1\7"+
    "\23\0\1\7\5\0\15\7\1\203\12\7\1\0\1\7"+
    "\23\0\1\7\5\0\13\7\1\204\14\7\1\0\1\7"+
    "\23\0\1\7\5\0\17\7\1\205\10\7\1\0\1\7"+
    "\23\0\1\7\2\0\1\206\1\207\1\0\34\206\1\210"+
    "\1\211\20\206\5\0\1\212\27\7\1\0\1\7\23\0"+
    "\1\7\5\0\1\7\1\213\26\7\1\0\1\7\23\0"+
    "\1\7\5\0\1\214\27\7\1\0\1\7\23\0\1\7"+
    "\5\0\14\7\1\215\13\7\1\0\1\7\23\0\1\7"+
    "\5\0\5\7\1\216\22\7\1\0\1\7\23\0\1\7"+
    "\5\0\14\7\1\217\13\7\1\0\1\7\23\0\1\7"+
    "\5\0\1\220\27\7\1\0\1\7\23\0\1\7\5\0"+
    "\24\7\1\221\3\7\1\0\1\7\23\0\1\7\5\0"+
    "\3\7\1\222\24\7\1\0\1\7\23\0\1\7\5\0"+
    "\5\7\1\223\22\7\1\0\1\7\23\0\1\7\5\0"+
    "\3\7\1\224\24\7\1\0\1\7\23\0\1\7\2\0"+
    "\1\206\121\0\1\225\23\0\1\206\1\207\1\0\34\206"+
    "\1\226\21\206\5\0\5\7\1\227\22\7\1\0\1\7"+
    "\23\0\1\7\5\0\14\7\1\230\13\7\1\0\1\7"+
    "\23\0\1\7\5\0\2\7\1\231\25\7\1\0\1\7"+
    "\23\0\1\7\5\0\3\7\1\232\24\7\1\0\1\7"+
    "\23\0\1\7\5\0\12\7\1\153\15\7\1\0\1\7"+
    "\23\0\1\7\5\0\16\7\1\233\11\7\1\0\1\7"+
    "\23\0\1\7\5\0\16\7\1\234\11\7\1\0\1\7"+
    "\23\0\1\7\5\0\5\7\1\235\22\7\1\0\1\7"+
    "\23\0\1\7\5\0\12\7\1\151\15\7\1\0\1\7"+
    "\23\0\1\7\5\0\1\236\27\7\1\0\1\7\23\0"+
    "\1\7\5\0\1\7\1\153\26\7\1\0\1\7\23\0"+
    "\1\7\41\0\1\237\23\0\1\206\1\207\1\0\34\206"+
    "\1\240\1\211\20\206\5\0\15\7\1\241\12\7\1\0"+
    "\1\7\23\0\1\7\5\0\3\7\1\242\24\7\1\0"+
    "\1\7\23\0\1\7\5\0\1\7\1\243\26\7\1\0"+
    "\1\7\23\0\1\7\5\0\15\7\1\244\12\7\1\0"+
    "\1\7\23\0\1\7\5\0\11\7\1\245\16\7\1\0"+
    "\1\7\23\0\1\7\5\0\14\7\1\246\13\7\1\0"+
    "\1\7\23\0\1\7\5\0\5\7\1\247\22\7\1\0"+
    "\1\7\23\0\1\7\41\0\1\250\26\0\24\7\1\153"+
    "\3\7\1\0\1\7\23\0\1\7\5\0\3\7\1\251"+
    "\24\7\1\0\1\7\23\0\1\7\5\0\5\7\1\252"+
    "\22\7\1\0\1\7\23\0\1\7\5\0\12\7\1\253"+
    "\15\7\1\0\1\7\23\0\1\7\5\0\3\7\1\254"+
    "\24\7\1\0\1\7\23\0\1\7\2\0\1\206\1\207"+
    "\1\0\34\206\1\255\1\211\20\206\5\0\6\7\1\153"+
    "\21\7\1\0\1\7\23\0\1\7";

  private static int [] zzUnpackTrans() {
    int [] result = new int[7089];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\23\1\1\11\2\1\14\11\30\1\3\0"+
    "\2\1\1\0\34\1\1\0\1\1\1\11\1\0\25\1"+
    "\1\0\16\1\3\0\13\1\1\0\11\1\1\11\1\0"+
    "\15\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[173];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _SmithyLexer() {
    this((java.io.Reader)null);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public _SmithyLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return BAD_CHARACTER;
            } 
            // fall through
          case 39: break;
          case 2: 
            { return WHITE_SPACE;
            } 
            // fall through
          case 40: break;
          case 3: 
            { return TOKEN_SYMBOL;
            } 
            // fall through
          case 41: break;
          case 4: 
            { return TOKEN_NUMBER;
            } 
            // fall through
          case 42: break;
          case 5: 
            { return TOKEN_PERIOD;
            } 
            // fall through
          case 43: break;
          case 6: 
            { return TOKEN_OPEN_PAREN;
            } 
            // fall through
          case 44: break;
          case 7: 
            { return TOKEN_CLOSE_PAREN;
            } 
            // fall through
          case 45: break;
          case 8: 
            { return TOKEN_OPEN_BRACE;
            } 
            // fall through
          case 46: break;
          case 9: 
            { return TOKEN_CLOSE_BRACE;
            } 
            // fall through
          case 47: break;
          case 10: 
            { return TOKEN_OPEN_BRACKET;
            } 
            // fall through
          case 48: break;
          case 11: 
            { return TOKEN_CLOSE_BRACKET;
            } 
            // fall through
          case 49: break;
          case 12: 
            { return TOKEN_COLON;
            } 
            // fall through
          case 50: break;
          case 13: 
            { return TOKEN_COMMA;
            } 
            // fall through
          case 51: break;
          case 14: 
            { return TOKEN_EQUALS;
            } 
            // fall through
          case 52: break;
          case 15: 
            { return TOKEN_DOLLAR_SIGN;
            } 
            // fall through
          case 53: break;
          case 16: 
            { return TOKEN_AT;
            } 
            // fall through
          case 54: break;
          case 17: 
            { return TOKEN_HASH;
            } 
            // fall through
          case 55: break;
          case 18: 
            { return TOKEN_INCOMPLETE_STRING;
            } 
            // fall through
          case 56: break;
          case 19: 
            { return TOKEN_STRING;
            } 
            // fall through
          case 57: break;
          case 20: 
            { return TOKEN_LINE_COMMENT;
            } 
            // fall through
          case 58: break;
          case 21: 
            { return TOKEN_USE;
            } 
            // fall through
          case 59: break;
          case 22: 
            { return TOKEN_SET;
            } 
            // fall through
          case 60: break;
          case 23: 
            { return TOKEN_MAP;
            } 
            // fall through
          case 61: break;
          case 24: 
            { return TOKEN_DOCUMENTATION_LINE;
            } 
            // fall through
          case 62: break;
          case 25: 
            { return TOKEN_BOOLEAN;
            } 
            // fall through
          case 63: break;
          case 26: 
            { return TOKEN_SIMPLE_TYPE_NAME;
            } 
            // fall through
          case 64: break;
          case 27: 
            { return TOKEN_LIST;
            } 
            // fall through
          case 65: break;
          case 28: 
            { return TOKEN_NULL;
            } 
            // fall through
          case 66: break;
          case 29: 
            { return TOKEN_UNION;
            } 
            // fall through
          case 67: break;
          case 30: 
            { return TOKEN_APPLY;
            } 
            // fall through
          case 68: break;
          case 31: 
            { return TOKEN_INCOMPLETE_TEXT_BLOCK;
            } 
            // fall through
          case 69: break;
          case 32: 
            { return TOKEN_SERVICE;
            } 
            // fall through
          case 70: break;
          case 33: 
            { return TOKEN_TEXT_BLOCK;
            } 
            // fall through
          case 71: break;
          case 34: 
            { return TOKEN_RESOURCE;
            } 
            // fall through
          case 72: break;
          case 35: 
            { return TOKEN_METADATA;
            } 
            // fall through
          case 73: break;
          case 36: 
            { return TOKEN_STRUCTURE;
            } 
            // fall through
          case 74: break;
          case 37: 
            { return TOKEN_OPERATION;
            } 
            // fall through
          case 75: break;
          case 38: 
            { return TOKEN_NAMESPACE;
            } 
            // fall through
          case 76: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
