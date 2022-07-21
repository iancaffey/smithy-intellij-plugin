package software.amazon.smithy.intellij.selector

import com.intellij.lang.injection.general.Injection
import com.intellij.lang.injection.general.LanguageInjectionContributor
import com.intellij.lang.injection.general.SimpleInjection
import com.intellij.psi.PsiElement
import software.amazon.smithy.intellij.psi.SmithyString

/**
 * @author Ian Caffey
 * @since 1.0
 */
class SmithySelectorInjectionContributor : LanguageInjectionContributor {
    override fun getInjection(context: PsiElement): Injection? {
        if (context is SmithyString) {
            return SimpleInjection(SmithySelectorLanguage, "\"", "\"", null)
        }
        return null
    }
}