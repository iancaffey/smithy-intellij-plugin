package software.amazon.smithy.intellij

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import software.amazon.smithy.intellij.psi.SmithyControl
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyMemberDefinition
import software.amazon.smithy.intellij.psi.SmithyMetadata
import software.amazon.smithy.intellij.psi.SmithyNamedElement
import software.amazon.smithy.intellij.psi.SmithyShapeDefinition
import software.amazon.smithy.intellij.psi.SmithyTypes


/**
 * A [FindUsagesProvider] for all [SmithyNamedElement] in [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner() = DefaultWordsScanner(
        SmithyLexer(),
        TokenSet.create(
            SmithyTypes.TOKEN_APPLY,
            SmithyTypes.TOKEN_BOOLEAN,
            SmithyTypes.TOKEN_LIST,
            SmithyTypes.TOKEN_MAP,
            SmithyTypes.TOKEN_METADATA,
            SmithyTypes.TOKEN_NAMESPACE,
            SmithyTypes.TOKEN_NULL,
            SmithyTypes.TOKEN_OPERATION,
            SmithyTypes.TOKEN_RESOURCE,
            SmithyTypes.TOKEN_SERVICE,
            SmithyTypes.TOKEN_SET,
            SmithyTypes.TOKEN_SIMPLE_TYPE_NAME,
            SmithyTypes.TOKEN_STRUCTURE,
            SmithyTypes.TOKEN_UNION,
            SmithyTypes.TOKEN_USE
        ),
        TokenSet.create(SmithyTypes.TOKEN_DOCUMENTATION_LINE, SmithyTypes.TOKEN_LINE_COMMENT),
        TokenSet.create(
            SmithyTypes.TOKEN_BOOLEAN,
            SmithyTypes.TOKEN_NULL,
            SmithyTypes.TOKEN_NUMBER,
            SmithyTypes.TOKEN_STRING,
            SmithyTypes.TOKEN_TEXT_BLOCK
        ),
    )

    override fun canFindUsagesFor(element: PsiElement) = element is SmithyNamedElement
    override fun getHelpId(element: PsiElement): String? = null
    override fun getDescriptiveName(element: PsiElement) = (element as? SmithyNamedElement)?.name ?: ""
    override fun getNodeText(element: PsiElement, useFullName: Boolean) = getDescriptiveName(element)
    override fun getType(element: PsiElement): String = when (element) {
        is SmithyControl -> "control"
        is SmithyEntry -> "entry"
        is SmithyMemberDefinition -> "member"
        is SmithyMetadata -> "metadata"
        is SmithyShapeDefinition -> "shape"
        else -> ""
    }
}
