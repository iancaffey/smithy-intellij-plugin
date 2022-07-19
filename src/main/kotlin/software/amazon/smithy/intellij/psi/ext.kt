package software.amazon.smithy.intellij.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil.getChildOfType
import com.intellij.psi.util.PsiTreeUtil.getChildrenOfTypeAsList
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import com.intellij.psi.util.siblings
import software.amazon.smithy.intellij.SmithyColorSettings
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.SmithyKeyReference
import software.amazon.smithy.intellij.SmithyLanguage
import software.amazon.smithy.intellij.SmithyMemberReference
import software.amazon.smithy.intellij.SmithyShapeReference
import software.amazon.smithy.intellij.SmithyShapeResolver.getNamespace
import software.amazon.smithy.intellij.SmithyValueDefinition
import software.amazon.smithy.intellij.SmithyValueType
import software.amazon.smithy.intellij.psi.impl.SmithyAggregateShapeImpl
import software.amazon.smithy.intellij.psi.impl.SmithyKeyedElementImpl
import software.amazon.smithy.intellij.psi.impl.SmithyPrimitiveImpl
import software.amazon.smithy.intellij.psi.impl.SmithyShapeImpl
import software.amazon.smithy.intellij.psi.impl.SmithyValueImpl
import java.math.BigDecimal
import java.math.BigInteger

/**
 * A base [PsiElement] for all [SmithyElement] implementations.
 *
 * @author Ian Caffey
 * @since 1.0
 */
open class SmithyPsiElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun toString(): String = name ?: text
}

interface SmithyElement : PsiElement
interface SmithyContainer : SmithyElement
interface SmithyNamedElement : SmithyElement, PsiNameIdentifierOwner
interface SmithyStatement : SmithyElement {
    val type: String get() = typeIdentifier.text
    val typeIdentifier: PsiElement get() = firstChild
}

abstract class SmithyAggregateShapeMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyAggregateShape {
    override val members: List<SmithyMember> get() = body.members
}

interface SmithyAppliedTraitExt : SmithyStatement
abstract class SmithyAppliedTraitMixin(node: ASTNode) : SmithyPsiElement(node), SmithyAppliedTrait

abstract class SmithyArrayMixin(node: ASTNode) : SmithyValueImpl(node), SmithyArray {
    override val type = SmithyValueType.ARRAY
}

interface SmithyBooleanExt : SmithyPrimitive {
    override fun asBoolean(): Boolean
}

abstract class SmithyBooleanMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyBoolean {
    override val type = SmithyValueType.BOOLEAN
    override fun asBoolean() = text.toBoolean()
}

interface SmithyControlExt : SmithyStatement
abstract class SmithyControlMixin(node: ASTNode) : SmithyKeyedElementImpl(node), SmithyControl

interface SmithyDocumentationExt : SmithyDocumentationDefinition

abstract class SmithyDocumentationMixin(node: ASTNode) : SmithyPsiElement(node), SmithyDocumentation {
    companion object {
        private val DOCUMENTATION_LINES = TokenSet.create(SmithyTypes.TOKEN_DOCUMENTATION_LINE)
    }

    override fun getOwner(): PsiElement = parent
    override fun getTokenType(): IElementType = SmithyTypes.DOCUMENTATION

    //see: https://awslabs.github.io/smithy/1.0/spec/core/idl.html#documentation-comment
    override fun toDocString() = node.getChildren(DOCUMENTATION_LINES).joinToString("\n") { node ->
        node.text.let { it.substring(if (it.length > 3 && it[3] == ' ') 4 else 3) }
    }
}

interface SmithyEntryExt : SmithyElement {
    fun resolve(): SmithyMemberDefinition?
}

abstract class SmithyEntryMixin(node: ASTNode) : SmithyKeyedElementImpl(node), SmithyEntry {
    override fun resolve() = key.reference.resolve()
}

interface SmithyImportExt : SmithyStatement
abstract class SmithyImportMixin(node: ASTNode) : SmithyPsiElement(node), SmithyImport

interface SmithyIncompleteAppliedTraitExt : SmithyStatement
abstract class SmithyIncompleteAppliedTraitMixin(node: ASTNode) : SmithyPsiElement(node), SmithyIncompleteAppliedTrait

interface SmithyKeyExt : SmithyElement {
    val reference: SmithyKeyReference
    fun stringValue(): String
}

abstract class SmithyKeyMixin(node: ASTNode) : SmithyPsiElement(node), SmithyKey {
    override val reference by lazy { SmithyKeyReference(this) }
    override fun stringValue(): String = id?.text ?: string?.asString() ?: text
}

interface SmithyKeyedElementExt : SmithyNamedElement {
    val nameIdentifier: SmithyKey
    override fun getName(): String
}

abstract class SmithyKeyedElementMixin(node: ASTNode) : SmithyPsiElement(node), SmithyKeyedElement {
    override val nameIdentifier get() = key
    override fun getName(): String = key.stringValue()
    override fun setName(newName: String) = setName<SmithyKeyedElement>(this, newName)
    override fun getTextOffset() = key.textOffset
}

abstract class SmithyListMixin(node: ASTNode) : SmithyAggregateShapeImpl(node), SmithyList {
    override val requiredMembers = setOf("member")
}

abstract class SmithyMapMixin(node: ASTNode) : SmithyAggregateShapeImpl(node), SmithyMap {
    override val requiredMembers = setOf("key", "value")
}

interface SmithyMemberExt : SmithyNamedElement, SmithyMemberDefinition {
    override val enclosingShape: SmithyShape
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
    override fun findTrait(namespace: String, shapeName: String): SmithyTrait?
}

abstract class SmithyMemberMixin(node: ASTNode) : SmithyPsiElement(node), SmithyMember {
    override val targetShapeName get() = shapeId.shapeName
    override val declaredTargetNamespace get() = shapeId.declaredNamespace
    override val resolvedTargetNamespace get() = shapeId.resolvedNamespace
    override val enclosingShape: SmithyAggregateShape get() = getParentOfType(this, SmithyAggregateShape::class.java)!!
    override fun getName(): String = nameIdentifier.text
    override fun setName(newName: String) = setName<SmithyMember>(this, newName)
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun resolve(): SmithyShapeDefinition? = shapeId.reference.resolve()
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = "$name: ${shapeId.shapeName}"
        override fun getLocationString() = (parent.parent as SmithyShape).shapeName
        override fun getIcon(unused: Boolean) = getIcon(0)
    }

    override fun findTrait(namespace: String, shapeName: String) = declaredTraits.find {
        it.shapeName == shapeName && it.resolvedNamespace == namespace
    }
}

interface SmithyMemberIdExt : SmithyElement {
    val reference: SmithyMemberReference
}

abstract class SmithyMemberIdMixin(node: ASTNode) : SmithyPsiElement(node), SmithyMemberId {
    override val reference by lazy { SmithyMemberReference(this) }
}

interface SmithyMetadataExt : SmithyStatement
abstract class SmithyMetadataMixin(node: ASTNode) : SmithyKeyedElementImpl(node), SmithyMetadata

interface SmithyModelExt : SmithyElement {
    val namespace: String
}

abstract class SmithyModelMixin(node: ASTNode) : SmithyPsiElement(node), SmithyModel {
    override val namespace get() = getChildOfType(this, SmithyNamespace::class.java)!!.namespaceId.id
}

interface SmithyNamespaceExt : SmithyStatement
abstract class SmithyNamespaceMixin(node: ASTNode) : SmithyPsiElement(node), SmithyNamespace

interface SmithyNamespaceIdExt : SmithyElement {
    val id: String
}

abstract class SmithyNamespaceIdMixin(node: ASTNode) : SmithyPsiElement(node), SmithyNamespaceId {
    override val id = parts.joinToString(".") { it.text }
    override fun toString() = id
}

abstract class SmithyNullMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyNull {
    override val type = SmithyValueType.NULL
}

interface SmithyNumberExt : SmithyPrimitive {
    fun byteValue(): Byte = bigDecimalValue().byteValueExact()
    fun shortValue(): Short = bigDecimalValue().shortValueExact()
    fun intValue(): Int = bigDecimalValue().intValueExact()
    fun floatValue(): Float = bigDecimalValue().toFloat()
    fun doubleValue(): Double = bigDecimalValue().toDouble()
    fun longValue(): Long = bigDecimalValue().longValueExact()
    fun bigDecimalValue(): BigDecimal

    fun bigIntegerValue(): BigInteger = bigDecimalValue().toBigIntegerExact()
}

abstract class SmithyNumberMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyNumber {
    override val type = SmithyValueType.NUMBER
    override fun asNumber() = bigDecimalValue()
    override fun bigDecimalValue() = BigDecimal(text)
}

abstract class SmithyObjectMixin(node: ASTNode) : SmithyValueImpl(node), SmithyObject {
    private var parsed = false
    private var value: Map<String, SmithyValue> = emptyMap()
    override val type = SmithyValueType.OBJECT
    override val fields
        get() = if (parsed) value else entries.associate { it.name to it.value }.also {
            value = it
            parsed = true
        }

    override fun subtreeChanged() {
        parsed = false
    }
}

abstract class SmithyOperationMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyOperation {
    override val supportedMembers = setOf("input", "output", "errors")
}

abstract class SmithyResourceMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyResource {
    override val supportedMembers = setOf(
        "identifiers",
        "create",
        "put",
        "read",
        "update",
        "delete",
        "list",
        "operations",
        "collectionOperations",
        "resources"
    )
}

abstract class SmithyServiceMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyService {
    override val supportedMembers = setOf("version", "operations", "resources", "errors", "rename")
}

abstract class SmithySetMixin(node: ASTNode) : SmithyAggregateShapeImpl(node), SmithySet {
    override val requiredMembers = setOf("member")
}

interface SmithyShapeExt : SmithyNamedElement, SmithyShapeDefinition, SmithyStatement {
    override val type get() = super.type
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
    val model: SmithyModel
    val requiredMembers: Set<String> get() = emptySet()
    val supportedMembers: Set<String>? get() = requiredMembers.takeIf { it.isNotEmpty() }
    override fun findTrait(namespace: String, shapeName: String): SmithyTraitDefinition?
}

abstract class SmithyShapeMixin(node: ASTNode) : SmithyPsiElement(node), SmithyShape {
    override val typeIdentifier
        get() = nameIdentifier.siblings(forward = false, withSelf = false).first {
            it !is PsiWhiteSpace && it !is PsiComment
        }
    override val namespace get() = model.namespace
    override val shapeName: String get() = nameIdentifier.text
    override val shapeId get() = "$namespace#$shapeName"
    override val model get() = parent as SmithyModel
    override val members get() = emptyList<SmithyMember>()
    override val documentation get() = getChildOfType(this, SmithyDocumentation::class.java)
    override val declaredTraits: List<SmithyTrait> get() = getChildrenOfTypeAsList(this, SmithyTrait::class.java)
    override fun getMember(name: String): SmithyMember? = members.find { it.name == name }
    override fun getName() = shapeName
    override fun setName(newName: String) = setName<SmithyShape>(this, newName)
    override fun getNameIdentifier() = getChildOfType(this, SmithyShapeName::class.java)!!
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = shapeName
        override fun getLocationString(): String = namespace
        override fun getIcon(unused: Boolean) = getIcon(0)
    }

    override fun findTrait(namespace: String, shapeName: String) = declaredTraits.find {
        it.shapeName == shapeName && it.resolvedNamespace == namespace
    }
}

interface SmithyShapeIdExt : SmithyCharSequence {
    val shapeName: String
    val declaredNamespace: String?
    val enclosingNamespace: String?
    val resolvedNamespace: String?
}

abstract class SmithyShapeIdMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyShapeId {
    override val shapeName: String get() = getChildOfType(this, SmithyId::class.java)!!.text
    override val declaredNamespace get() = namespaceId?.id
    override val enclosingNamespace get() = (containingFile as SmithyFile).model?.namespace
    override val resolvedNamespace: String? get() = declaredNamespace ?: getNamespace(this, shapeName)
    override fun asString() = resolvedNamespace?.let { "$it#$shapeName" } ?: shapeName
}

private fun parseInnerText(text: String, start: Int = 0, end: Int = text.length - start): String? = buildString {
    var i = start
    val lastIndex = end.takeIf { it >= i } ?: return null
    while (i < lastIndex) {
        val n = i + 1
        val c = text[i]
        if (c == '\r') {
            append('\n').also { i += if (n < lastIndex && text[n] == '\n') 2 else 1 }
        } else if (c != '\\') {
            append(c).also { i++ }
        } else if (i == lastIndex - 1) {
            return null //invalid string (closing quote is escaped)
        } else when (text[n]) {
            '\n' -> i += 2 //escaped new-line expands to nothing
            '\r' -> i += if (n < lastIndex - 1 && text[n + 1] == '\n') 3 else 2 //escaped new-line expands to nothing
            '"' -> append('"').also { i += 2 }
            '\\' -> append('\\').also { i += 2 }
            '/' -> append('/').also { i += 2 }
            'b' -> append('\b').also { i += 2 }
            'f' -> append('\u000c').also { i += 2 }
            'n' -> append('\n').also { i += 2 }
            'r' -> append('\r').also { i += 2 }
            't' -> append('\t').also { i += 2 }
            'u' -> {
                if (n >= lastIndex - 4) return null //invalid string (less than 4 digits in the unicode escape sequence)
                var unicode = 0
                (1..4).forEach {
                    unicode = when (val u = text[n + it]) {
                        in '0'..'9' -> (unicode shl 4) or (u - '0')
                        in 'a'..'f' -> (unicode shl 4) or (10 + (u - 'a'))
                        in 'A'..'F' -> (unicode shl 4) or (10 + (u - 'A'))
                        else -> return null //invalid string (non-digit in unicode escape sequence)
                    }
                }
                append(unicode.toChar()).also { i += 6 }
            }
            else -> return null //invalid string (unknown escape sequence)
        }
    }
}

interface SmithyCharSequence : SmithyPrimitive {
    override val type get() = SmithyValueType.STRING
    val valid: Boolean get() = asString() != null
}

interface SmithyStringExt : SmithyCharSequence

abstract class SmithyStringMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyString {
    private var parsed = false
    private var value: String? = null
    override fun asString(): String? =
        if (parsed) value else parseInnerText(text, 1).also {
            value = it
            parsed = true
        }

    override fun subtreeChanged() {
        parsed = false
    }
}

interface SmithyTextBlockExt : SmithyCharSequence

abstract class SmithyTextBlockMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyTextBlock {
    private var parsed = false
    private var value: String? = null
    override fun asString(): String? =
        if (parsed) value else text.let { text ->
            val normalized = if ('\r' in text) text.replace("\r\n", "\n") else text
            if (normalized.length < 7) return@let null
            val lines = normalized.substring(4, normalized.length - 3).split('\n')
            val leadingSpaces = lines.filterIndexed { i, line ->
                i == lines.lastIndex || line.any { it != ' ' }
            }.minOfOrNull { line -> line.indexOfFirst { it != ' ' }.takeIf { it != -1 } ?: line.length } ?: 0
            val trimmed = lines.joinToString("\n") { line ->
                line.let { if (line.length < leadingSpaces) line else line.substring(leadingSpaces) }.trimEnd(' ')
            }
            parseInnerText(trimmed)
        }.also {
            value = it
            parsed = true
        }

    override fun subtreeChanged() {
        parsed = false
    }
}

interface SmithyTraitExt : SmithyElement, SmithyTraitDefinition

abstract class SmithyTraitMixin(node: ASTNode) : SmithyPsiElement(node), SmithyTrait {
    override val shapeName get() = shape.shapeName
    override val declaredNamespace get() = shape.declaredNamespace
    override val resolvedNamespace get() = shape.resolvedNamespace
    override fun toDocString() = buildString {
        //Note: since this can only do lexer-based highlighting, this will be approximately the same style as
        //the editor display (but could have some keyword highlighting issues in the body)
        HtmlSyntaxInfoUtil.appendStyledSpan(
            this, SmithyColorSettings.TRAIT_NAME, "@$shapeName", 1f
        )
        body?.let { body ->
            //Note: since keys are dynamically highlighted using an annotator, this will manually apply the same
            //behavior to improve readability of trait values
            if (body.values.isNotEmpty()) {
                append(body.values.joinToString(", ", "(", ")") {
                    val key = HtmlSyntaxInfoUtil.getStyledSpan(
                        SmithyColorSettings.KEY, it.key.text, 1f
                    )
                    val value = HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                        project, SmithyLanguage, it.value.text, 1f
                    )
                    "$key: $value"
                })
            } else {
                HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                    this, project, SmithyLanguage, body.text, 1f
                )
            }
        }
    }
}

interface SmithyValueExt : SmithyElement, SmithyValueDefinition {
    val reference: SmithyShapeReference
}

abstract class SmithyValueMixin(node: ASTNode) : SmithyPsiElement(node), SmithyValue {
    override val reference by lazy { SmithyShapeReference(this) }
}

private fun <T : SmithyNamedElement> setName(element: T, newName: String?): T {
    val name = element.nameIdentifier ?: return element
    val textRange = name.textRange
    val document = FileDocumentManager.getInstance().getDocument(name.containingFile.virtualFile)
    document!!.replaceString(textRange.startOffset, textRange.endOffset, newName!!)
    PsiDocumentManager.getInstance(name.project).commitDocument(document)
    return element
}