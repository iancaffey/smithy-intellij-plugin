package software.amazon.smithy.intellij

import com.intellij.testFramework.ParsingTestCase
import org.junit.Test

@Suppress("JUnitMixedFramework")
class SmithyParserTest : ParsingTestCase("parsing", "smithy", SmithyParserDefinition()) {
    override fun skipSpaces() = true
    override fun includeRanges() = true
    override fun getTestDataPath() = "src/test/resources"

    @Test
    fun testBlank() = doTest(true)

    @Test
    fun testEmpty() = doTest(true)

    @Test
    fun testNamespaceWithUnderscoreFollowedByNumber() = doTest(true)

    @Test
    fun testSimple() = doTest(true)

    @Test
    fun testWeather() = doTest(true)
}
