package software.amazon.smithy.intellij

/**
 * A collection of commonly used shapes from the [Smithy](https://awslabs.github.io/smithy) [prelude](https://awslabs.github.io/smithy/1.0/spec/core/prelude-model.html#prelude-shapes).
 * @author Ian Caffey
 * @since 1.0
 */
object SmithyPreludeShapes {
    private fun shapeId(name: String) = "${NAMESPACE}#$name"

    val NAMESPACE = "smithy.api"
    val DEPRECATED = shapeId("deprecated")
    val DOCUMENTATION = shapeId("documentation")
    val EXTERNAL_DOCUMENTATION = shapeId("externalDocumentation")
    val PRIVATE = shapeId("private")
    val TRAIT = shapeId("trait")
    val UNSTABLE = shapeId("unstable")
}