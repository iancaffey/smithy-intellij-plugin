package software.amazon.smithy.intellij

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage


/**
 * A [ColorSettingsPage] to configure syntax highlighting for [Smithy](https://awslabs.github.io/smithy).
 *
 * @author Ian Caffey
 * @since 1.0
 */
class SmithyColorSettingsPage : ColorSettingsPage {
    companion object {
        val BRACES = createTextAttributesKey("SMITHY_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = createTextAttributesKey("SMITHY_BRACKET", DefaultLanguageHighlighterColors.BRACKETS)
        val COMMA = createTextAttributesKey("SMITHY_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val DOC_COMMENT = createTextAttributesKey("SMITHY_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT)
        val DOT = createTextAttributesKey("SMITHY_DOT", DefaultLanguageHighlighterColors.DOT)
        val IDENTIFIER = createTextAttributesKey("SMITHY_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val KEYWORD = createTextAttributesKey("SMITHY_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val LINE_COMMENT = createTextAttributesKey("SMITHY_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val NUMBER = createTextAttributesKey("SMITHY_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val PARENS = createTextAttributesKey("SMITHY_PARENS", DefaultLanguageHighlighterColors.PARENTHESES)
        val SEMICOLON = createTextAttributesKey("SMITHY_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val STRING = createTextAttributesKey("SMITHY_STRING", DefaultLanguageHighlighterColors.STRING)
        val TRAIT_NAME = createTextAttributesKey("SMITHY_TRAIT_NAME", DefaultLanguageHighlighterColors.METADATA)
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Braces", BRACES),
            AttributesDescriptor("Brackets", BRACKETS),
            AttributesDescriptor("Comma", COMMA),
            AttributesDescriptor("Documentation comment", DOC_COMMENT),
            AttributesDescriptor("Dot", DOT),
            AttributesDescriptor("Keyword", KEYWORD),
            AttributesDescriptor("Line comment", LINE_COMMENT),
            AttributesDescriptor("Number", NUMBER),
            AttributesDescriptor("Parenthesis", PARENS),
            AttributesDescriptor("Semicolon", SEMICOLON),
            AttributesDescriptor("String", STRING),
            AttributesDescriptor("Trait", TRAIT_NAME)
        )
    }

    override fun getDisplayName() = "Smithy"
    override fun getIcon() = SmithyIcons.FILE
    override fun getHighlighter() = SmithySyntaxHighlighter()
    override fun getAdditionalHighlightingTagToDescriptorMap() = emptyMap<String, TextAttributesKey>()
    override fun getAttributeDescriptors() = DESCRIPTORS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDemoText() = """
namespace example.weather

/// Provides weather forecasts.
@paginated(inputToken: "nextToken", outputToken: "nextToken", pageSize: "pageSize")
service Weather {
    version: "2006-03-01",
    resources: [City],
    operations: [GetCurrentTime]
}

resource City {
    identifiers: { cityId: CityId },
    read: GetCity,
    list: ListCities,
    resources: [Forecast],
}

resource Forecast {
    identifiers: { cityId: CityId },
    read: GetForecast,
}

// "pattern" is a trait.
@pattern("^[A-Za-z0-9 ]+$\{'$'}")
string CityId

@readonly
operation GetCity {
    input: GetCityInput,
    output: GetCityOutput,
    errors: [NoSuchResource]
}

@input
structure GetCityInput {
    // "cityId" provides the identifier for the resource and
    // has to be marked as required.
    @required
    cityId: CityId
}

@output
structure GetCityOutput {
    // "required" is used on output to indicate if the service
    // will always provide a value for the member.
    @required
    name: String,

    @required
    coordinates: CityCoordinates,
}

// This structure is nested within GetCityOutput.
structure CityCoordinates {
    @required
    latitude: Float,

    @required
    longitude: Float,
}

// "error" is a trait that is used to specialize
// a structure as an error.
@error("client")
structure NoSuchResource {
    @required
    resourceType: String
}

// The paginated trait indicates that the operation may
// return truncated results.
@readonly
@paginated(items: "items")
operation ListCities {
    input: ListCitiesInput,
    output: ListCitiesOutput
}

@input
structure ListCitiesInput {
    nextToken: String,
    pageSize: Integer
}

@output
structure ListCitiesOutput {
    nextToken: String,

    @required
    items: CitySummaries,
}

// CitySummaries is a list of CitySummary structures.
list CitySummaries {
    member: CitySummary
}

// CitySummary contains a reference to a City.
@references([{resource: City}])
structure CitySummary {
    @required
    cityId: CityId,

    @required
    name: String,
}

@readonly
operation GetCurrentTime {
    input: GetCurrentTimeInput,
    output: GetCurrentTimeOutput
}

@input
structure GetCurrentTimeInput {}

@output
structure GetCurrentTimeOutput {
    @required
    time: Timestamp
}

@readonly
operation GetForecast {
    input: GetForecastInput,
    output: GetForecastOutput
}

// "cityId" provides the only identifier for the resource since
// a Forecast doesn't have its own.
@input
structure GetForecastInput {
    @required
    cityId: CityId,
}

@output
structure GetForecastOutput {
    chanceOfRain: Float
}
""".trimIndent()
}