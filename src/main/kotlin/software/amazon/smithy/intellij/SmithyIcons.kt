package software.amazon.smithy.intellij

import com.intellij.openapi.util.IconLoader

/**
 * A utility class providing icons used within the [Smithy](https://awslabs.github.io/smithy) plugin.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyIcons {
    val FILE = icon("file")
    val LOGO = icon("plugin")
    val MEMBER = icon("member")
    val SHAPE = icon("shape")
    val TRAIT = icon("trait")

    private fun icon(name: String) = IconLoader.getIcon("META-INF/${name}Icon.svg", SmithyIcons.javaClass)
}