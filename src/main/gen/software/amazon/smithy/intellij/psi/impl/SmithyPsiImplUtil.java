package software.amazon.smithy.intellij.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import software.amazon.smithy.intellij.psi.SmithyBoolean;
import software.amazon.smithy.intellij.psi.SmithyDocumentation;
import software.amazon.smithy.intellij.psi.SmithyNumber;
import software.amazon.smithy.intellij.psi.SmithyTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringJoiner;

/**
 * A utility class providing mixins for AST nodes (generated by <a href="https://github.com/JetBrains/Grammar-Kit">Grammar-Kit</a>).
 * <p>
 * All {@link SmithyNumber} value-conversion methods perform safe parsing (by using {@link BigDecimal}) but will fail
 * with an {@link ArithmeticException} if converting to an integral integer type and the number is too large to fit.
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class SmithyPsiImplUtil {
    private static final TokenSet DOCUMENTATION_LINES = TokenSet.create(SmithyTypes.TOKEN_DOCUMENTATION_LINE);

    public static boolean booleanValue(SmithyBoolean b) {
        return Boolean.parseBoolean(b.getText());
    }

    public static double byteValue(SmithyNumber number) {
        return bigDecimalValue(number).byteValueExact();
    }

    public static double shortValue(SmithyNumber number) {
        return bigDecimalValue(number).shortValueExact();
    }

    public static double intValue(SmithyNumber number) {
        return bigDecimalValue(number).intValueExact();
    }

    public static float floatValue(SmithyNumber number) {
        return bigDecimalValue(number).floatValue();
    }

    public static double doubleValue(SmithyNumber number) {
        return bigDecimalValue(number).doubleValue();
    }

    public static long longValue(SmithyNumber number) {
        return bigDecimalValue(number).longValueExact();
    }

    public static BigDecimal bigDecimalValue(SmithyNumber number) {
        return new BigDecimal(number.getText());
    }

    public static BigInteger bigIntegerValue(SmithyNumber number) {
        return bigDecimalValue(number).toBigIntegerExact();
    }

    public static PsiElement getOwner(SmithyDocumentation documentation) {
        return documentation.getParent();
    }

    public static IElementType getTokenType(SmithyDocumentation documentation) {
        return SmithyTypes.DOCUMENTATION;
    }

    public static String toDocString(SmithyDocumentation documentation) {
        //see: https://awslabs.github.io/smithy/1.0/spec/core/idl.html#documentation-comment
        StringJoiner joiner = new StringJoiner("\n");
        for (ASTNode child : documentation.getNode().getChildren(DOCUMENTATION_LINES)) {
            String text = child.getText();
            joiner.add(text.substring(text.length() > 3 && text.charAt(3) == ' ' ? 4 : 3));
        }
        return joiner.toString();
    }
}
