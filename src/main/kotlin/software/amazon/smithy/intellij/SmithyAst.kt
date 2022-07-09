package software.amazon.smithy.intellij

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.InputStream

/**
 * An [abstract syntax tree](https://awslabs.github.io/smithy/1.0/spec/core/json-ast.html) for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
data class SmithyAst(
    @JsonProperty("smithy") val version: kotlin.String,
    val metadata: kotlin.collections.Map<kotlin.String, Any>? = null,
    val shapes: kotlin.collections.Map<kotlin.String, Shape>? = null
) {
    fun toJson() = toJson(this)

    companion object {
        private val serializer = jacksonObjectMapper().apply {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            setSerializationInclusion(Include.NON_NULL)
        }

        fun parse(input: InputStream): SmithyAst {
            return serializer.readValue(input, SmithyAst::class.java)
        }

        fun parse(file: File): SmithyAst {
            return serializer.readValue(file, SmithyAst::class.java)
        }

        fun parse(ast: kotlin.String): SmithyAst {
            return serializer.readValue(ast, SmithyAst::class.java)
        }

        fun toJson(value: Any?): kotlin.String = serializer.writeValueAsString(value)
    }

    data class Reference(val target: kotlin.String, val traits: kotlin.collections.Map<kotlin.String, Any>? = null)

    @JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
    @JsonSubTypes(
        Type(value = Blob::class, name = "blob"),
        Type(value = Boolean::class, name = "boolean"),
        Type(value = Document::class, name = "document"),
        Type(value = String::class, name = "string"),
        Type(value = Byte::class, name = "byte"),
        Type(value = Short::class, name = "short"),
        Type(value = Integer::class, name = "integer"),
        Type(value = Long::class, name = "long"),
        Type(value = Float::class, name = "float"),
        Type(value = Double::class, name = "double"),
        Type(value = BigInteger::class, name = "bigInteger"),
        Type(value = BigDecimal::class, name = "bigDecimal"),
        Type(value = Timestamp::class, name = "timestamp"),
        Type(value = List::class, name = "list"),
        Type(value = Set::class, name = "set"),
        Type(value = Map::class, name = "map"),
        Type(value = Structure::class, name = "structure"),
        Type(value = Union::class, name = "union"),
        Type(value = Service::class, name = "service"),
        Type(value = Resource::class, name = "resource"),
        Type(value = Operation::class, name = "operation"),
        Type(value = AppliedTrait::class, name = "apply")
    )
    sealed interface Shape {
        val type: kotlin.String
        val traits: kotlin.collections.Map<kotlin.String, Any>?
    }

    sealed interface Collection : AggregateShape {
        val member: Reference
    }

    sealed interface AggregateShape : Shape {
        val members: kotlin.collections.Map<kotlin.String, Reference>?
    }

    data class Blob(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "blob"
    }

    data class Boolean(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "boolean"
    }

    data class Document(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "document"
    }

    data class String(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "string"
    }

    data class Byte(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "byte"
    }

    data class Short(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "short"
    }

    data class Integer(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "integer"
    }

    data class Long(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "long"
    }

    data class Float(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "float"
    }

    data class Double(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "double"
    }

    data class BigInteger(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "bigInteger"
    }

    data class BigDecimal(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "bigDecimal"
    }

    data class Timestamp(override val traits: kotlin.collections.Map<kotlin.String, Any>? = null) : Shape {
        @JsonIgnore
        override val type = "timestamp"
    }

    data class List(
        override val member: Reference, override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : Collection {
        @JsonIgnore
        override val type = "list"

        @JsonIgnore
        override val members = mapOf("member" to member)
    }

    data class Set(
        override val member: Reference, override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : Collection {
        @JsonIgnore
        override val type = "set"

        @JsonIgnore
        override val members = mapOf("member" to member)
    }

    data class Map(
        val key: Reference,
        val value: Reference,
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : AggregateShape {
        @JsonIgnore
        override val type = "map"

        @JsonIgnore
        override val members = mapOf("key" to key, "value" to value)
    }

    data class Structure(
        override val members: kotlin.collections.Map<kotlin.String, Reference>? = null,
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : AggregateShape {
        @JsonIgnore
        override val type = "structure"
    }

    data class Union(
        override val members: kotlin.collections.Map<kotlin.String, Reference>? = null,
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : AggregateShape {
        @JsonIgnore
        override val type = "union"
    }

    data class Service(
        val version: kotlin.String,
        val operations: kotlin.collections.List<Reference>? = null,
        val resources: kotlin.collections.List<Reference>? = null,
        val errors: kotlin.collections.List<Reference>? = null,
        val rename: kotlin.collections.Map<kotlin.String, kotlin.String>? = null,
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : Shape {
        @JsonIgnore
        override val type = "service"
    }

    data class Resource(
        val identifiers: kotlin.collections.Map<kotlin.String, Reference>? = null,
        val create: Reference? = null,
        val put: Reference? = null,
        val read: Reference? = null,
        val update: Reference? = null,
        val delete: Reference? = null,
        val list: Reference? = null,
        val operations: kotlin.collections.List<Reference>? = null,
        val collectionOperations: kotlin.collections.List<Reference>? = null,
        val resources: kotlin.collections.List<Reference>? = null,
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null,
    ) : Shape {
        @JsonIgnore
        override val type = "resource"
    }

    data class Operation(
        val input: Reference? = null,
        val output: Reference? = null,
        val errors: kotlin.collections.List<Reference>? = null,
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : Shape {
        @JsonIgnore
        override val type = "operation"
    }

    data class AppliedTrait(
        override val traits: kotlin.collections.Map<kotlin.String, Any>? = null
    ) : Shape {
        @JsonIgnore
        override val type = "apply"
    }
}
