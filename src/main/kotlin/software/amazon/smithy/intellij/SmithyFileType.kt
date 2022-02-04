package software.amazon.smithy.intellij

import com.intellij.openapi.fileTypes.LanguageFileType

/**
 * A [LanguageFileType] for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyFileType : LanguageFileType(SmithyLanguage) {
    override fun getName() = "Smithy"
    override fun getDescription() = "A language for defining services and SDKs"
    override fun getDefaultExtension() = "smithy"
    override fun getIcon() = SmithyIcons.FILE
}