// This is a generated file. Not intended for manual editing.
package software.amazon.smithy.intellij.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiDocCommentBase;
import com.intellij.psi.tree.IElementType;

public interface SmithyDocumentation extends PsiDocCommentBase, SmithyElement {

  PsiElement getOwner();

  @NotNull IElementType getTokenType();

  @NotNull String toDocString();

}
