package software.amazon.smithy.intellij

import com.intellij.openapi.util.IconLoader

/**
 * A utility class providing icons used within the [Smithy](https://awslabs.github.io/smithy) plugin.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyIcons {
    val FILE = IconLoader.getIcon("META-INF/fileIcon.svg", SmithyIcons.javaClass)
    val LOGO = IconLoader.getIcon("META-INF/pluginIcon.svg", SmithyIcons.javaClass)
}