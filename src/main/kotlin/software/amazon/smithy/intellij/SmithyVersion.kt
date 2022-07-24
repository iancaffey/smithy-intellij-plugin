package software.amazon.smithy.intellij

/**
 * A utility class for comparing and parsing [Smithy](https://awslabs.github.io/smithy) [version](https://awslabs.github.io/smithy/1.0/spec/core/idl.html#version-statement) statements into a `{major}.{minor}` format.
 *
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyVersion {
    fun compare(left: String, right: String): Int {
        val normalizedLeft = if ('.' in left) left else "$left.0"
        val normalizedRight = if ('.' in right) right else "$right.0"
        return normalizedLeft.compareTo(normalizedRight)
    }

    fun parse(version: String): String? {
        val parts = version.split(".")
        return when (parts.size) {
            1 -> try {
                Integer.parseInt(parts[0])
                "${parts[0]}.0"
            } catch (e: Exception) {
                null
            }
            2 -> try {
                Integer.parseInt(parts[0])
                Integer.parseInt(parts[1])
                "${parts[0]}.${parts[1]}"
            } catch (e: Exception) {
                null
            }
            else -> null
        }
    }
}