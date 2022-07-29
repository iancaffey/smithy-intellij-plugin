package software.amazon.smithy.intellij.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil.getHighlightedByLexerAndEncodedAsHtmlCodeSnippet
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.FakePsiElement
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
import software.amazon.smithy.intellij.psi.impl.SmithyContainerShapeImpl
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
interface SmithyNamedElement : SmithyElement, PsiNameIdentifierOwner {
    override fun getName(): String
    override fun getNameIdentifier(): PsiElement
}

abstract class SmithySyntheticElement : FakePsiElement(), SmithyElement {
    override fun getLanguage() = SmithyLanguage
}

interface SmithyStatement : SmithyElement {
    val type: String get() = typeIdentifier.text
    val typeIdentifier: PsiElement get() = firstChild
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

interface SmithyContainerMemberExt : SmithyNamedElement, SmithyMemberDefinition {
    override val enclosingShape: SmithyShape
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
}

abstract class SmithyContainerMemberMixin(node: ASTNode) : SmithyPsiElement(node), SmithyContainerMember {
    override val enclosingShape: SmithyContainerShape get() = getParentOfType(this, SmithyContainerShape::class.java)!!
    override fun getName(): String = nameIdentifier.text
    override fun setName(newName: String) = setName<SmithyContainerMember>(this, newName)
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = resolvedTarget?.let { "$name: ${it.shapeName}" } ?: name
        override fun getLocationString() = enclosingShape.shapeName
        override fun getIcon(unused: Boolean) = getIcon(0)
    }
}

interface SmithyContainerShapeExt : SmithyShape {
    override val members: List<SmithyContainerMember>
}

abstract class SmithyContainerShapeMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyContainerShape {
    override val members: List<SmithyContainerMember> get() = body.members
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

interface SmithyEnumExt : SmithyShape {
    override val members: List<SmithyEnumMember>
}

abstract class SmithyEnumMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyEnum {
    override val members: List<SmithyEnumMember> get() = body.members
}

interface SmithyEnumMemberExt : SmithyNamedElement, SmithyMemberDefinition {
    override val enclosingShape: SmithyEnum
    override val declaredTarget: SmithySyntheticShapeTarget
    override val resolvedTarget: SmithySyntheticShapeTarget
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
}

abstract class SmithyEnumMemberMixin(node: ASTNode) : SmithyPsiElement(node), SmithyEnumMember {
    override val declaredTarget = SmithySyntheticShapeTarget(this, "string")
    override val resolvedTarget = declaredTarget
    override val enclosingShape: SmithyEnum get() = getParentOfType(this, SmithyEnum::class.java)!!
    override fun getName(): String = nameIdentifier.text
    override fun setName(newName: String) = setName<SmithyEnumMember>(this, newName)
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = name
        override fun getLocationString() = enclosingShape.shapeName
        override fun getIcon(unused: Boolean) = getIcon(0)
    }
}

interface SmithyImportExt : SmithyStatement
abstract class SmithyImportMixin(node: ASTNode) : SmithyPsiElement(node), SmithyImport

interface SmithyIncompleteAppliedTraitExt : SmithyStatement
abstract class SmithyIncompleteAppliedTraitMixin(node: ASTNode) : SmithyPsiElement(node), SmithyIncompleteAppliedTrait

interface SmithyIntEnumExt : SmithyShape {
    override val members: List<SmithyIntEnumMember>
}

abstract class SmithyIntEnumMixin(node: ASTNode) : SmithyShapeImpl(node), SmithyIntEnum {
    override val members: List<SmithyIntEnumMember> get() = body.members
}

interface SmithyIntEnumMemberExt : SmithyNamedElement, SmithyMemberDefinition {
    override val enclosingShape: SmithyIntEnum
    override val declaredTarget: SmithySyntheticShapeTarget
    override val resolvedTarget: SmithySyntheticShapeTarget
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
}

abstract class SmithyIntEnumMemberMixin(node: ASTNode) : SmithyPsiElement(node), SmithyIntEnumMember {
    override val declaredTarget = SmithySyntheticShapeTarget(this, "integer")
    override val resolvedTarget = declaredTarget
    override val enclosingShape: SmithyIntEnum get() = getParentOfType(this, SmithyIntEnum::class.java)!!
    override fun getName(): String = nameIdentifier.text
    override fun setName(newName: String) = setName<SmithyIntEnumMember>(this, newName)
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = name
        override fun getLocationString() = enclosingShape.shapeName
        override fun getIcon(unused: Boolean) = getIcon(0)
    }
}

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
}

abstract class SmithyKeyedElementMixin(node: ASTNode) : SmithyPsiElement(node), SmithyKeyedElement {
    override val nameIdentifier get() = key
    override fun getName(): String = key.stringValue()
    override fun setName(newName: String) = setName<SmithyKeyedElement>(this, newName)
    override fun getTextOffset() = key.textOffset
}

abstract class SmithyListMixin(node: ASTNode) : SmithyContainerShapeImpl(node), SmithyList {
    override val requiredMembers = setOf("member")
}

abstract class SmithyMapMixin(node: ASTNode) : SmithyContainerShapeImpl(node), SmithyMap {
    override val requiredMembers = setOf("key", "value")
}

interface SmithyMemberIdExt : SmithyElement {
    val memberName: String
    val reference: SmithyMemberReference
}

abstract class SmithyMemberIdMixin(node: ASTNode) : SmithyPsiElement(node), SmithyMemberId {
    override val memberName: String get() = member.text
    override val reference by lazy { SmithyMemberReference(this) }
}

interface SmithyMetadataExt : SmithyStatement
abstract class SmithyMetadataMixin(node: ASTNode) : SmithyKeyedElementImpl(node), SmithyMetadata

interface SmithyModelExt : SmithyElement {
    val namespace: String?
    val version: String?
    val operationInputSuffix: String
    val operationOutputSuffix: String
    val control: List<SmithyControl>
    val metadata: List<SmithyMetadata>
}

abstract class SmithyModelMixin(node: ASTNode) : SmithyPsiElement(node), SmithyModel {
    override val namespace get() = getChildOfType(this, SmithyNamespace::class.java)?.namespaceId?.id
    override val version get() = control.firstNotNullOfOrNull { if (it.name == "version") it.value.asString() else null }
    override val operationInputSuffix
        get() = control.firstNotNullOfOrNull {
            if (it.name == "operationInputSuffix") it.value.asString() else null
        } ?: "Input"
    override val operationOutputSuffix
        get() = control.firstNotNullOfOrNull {
            if (it.name == "operationOutputSuffix") it.value.asString() else null
        } ?: "Output"
    override val control: List<SmithyControl> get() = getChildrenOfTypeAsList(this, SmithyControl::class.java)
    override val metadata: List<SmithyMetadata> get() = getChildrenOfTypeAsList(this, SmithyMetadata::class.java)
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

abstract class SmithySetMixin(node: ASTNode) : SmithyContainerShapeImpl(node), SmithySet {
    override val requiredMembers = setOf("member")
}

interface SmithyShapeExt : SmithyNamedElement, SmithyShapeDefinition, SmithyStatement {
    override val type get() = super.type
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
    val model: SmithyModel
    val requiredMembers: Set<String> get() = emptySet()
    val supportedMembers: Set<String>? get() = requiredMembers.takeIf { it.isNotEmpty() }
    override fun getNameIdentifier(): SmithyShapeName
}

abstract class SmithyShapeMixin(node: ASTNode) : SmithyPsiElement(node), SmithyShape {
    override val typeIdentifier
        get() = nameIdentifier.siblings(forward = false, withSelf = false).first {
            it !is PsiWhiteSpace && it !is PsiComment
        }
    override val namespace get() = model.namespace!!
    override val shapeName: String get() = nameIdentifier.text
    override val shapeId get() = "$namespace#$shapeName"
    override val model get() = parent as SmithyModel
    override val members get(): List<@JvmWildcard SmithyMemberDefinition> = emptyList<SmithyMemberDefinition>()
    override val documentation get() = getChildOfType(this, SmithyDocumentation::class.java)
    override val declaredTraits: List<SmithyTrait> get() = getChildrenOfTypeAsList(this, SmithyTrait::class.java)
    override fun getMember(name: String): SmithyMemberDefinition? = members.find { it.name == name }
    override fun getName() = shapeName
    override fun setName(newName: String) = setName<SmithyShape>(this, newName)
    override fun getNameIdentifier() = getChildOfType(this, SmithyShapeName::class.java)!!
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = shapeName
        override fun getLocationString(): String = namespace
        override fun getIcon(unused: Boolean) = getIcon(0)
    }
}

interface SmithyShapeIdExt : SmithyCharSequence, SmithyShapeTarget, PsiNamedElement {
    val enclosingNamespace: String?
}

abstract class SmithyShapeIdMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyShapeId {
    override val shapeName: String get() = id.text
    override val declaredNamespace get() = namespaceId?.id
    override val enclosingNamespace get() = (containingFile as SmithyFile).model?.namespace
    override val resolvedNamespace: String? get() = declaredNamespace ?: getNamespace(shapeName, containingFile)
    override fun asString() = resolvedNamespace?.let { "$it#$shapeName" } ?: shapeName
    override fun getName() = shapeName
    override fun setName(newName: String) = setName<SmithyShapeId>(this, id, newName)
    override fun getTextOffset() = id.textOffset
    override fun resolve() = reference.resolve()
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

private class EmptyObject(private val element: PsiElement) : SmithySyntheticElement(), SmithyValueDefinition {
    override val type = SmithyValueType.OBJECT
    override fun getParent() = element
}

interface SmithyTraitExt : SmithyTraitDefinition

abstract class SmithyTraitMixin(node: ASTNode) : SmithyPsiElement(node), SmithyTrait {
    private var parsed = false
    private var _value: SmithyValueDefinition = EmptyObject(this)
    override val value: SmithyValueDefinition
        get() =
            if (parsed) _value else when (val body = body) {
                null -> EmptyObject(this)
                else -> body.value ?: object : SmithySyntheticElement(), SmithyValueDefinition {
                    override val type = SmithyValueType.OBJECT
                    override val fields = body.entries.associate { it.name to it.value }
                    override fun getParent() = this@SmithyTraitMixin
                }
            }.also {
                _value = it
                parsed = true
            }
    override val shapeName get() = shape.shapeName
    override val declaredNamespace get() = shape.declaredNamespace
    override val resolvedNamespace get() = shape.resolvedNamespace

    override fun subtreeChanged() {
        parsed = false
    }

    override fun getName() = shapeName
    override fun setName(newName: String) = also { shape.setName(newName) }
    override fun getTextOffset() = shape.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = shapeName
        override fun getLocationString(): String? = when (val parent = parent) {
            is SmithyAppliedTrait -> containingFile.name
            is SmithyDefinition -> parent.name
            else -> null
        }

        override fun getIcon(unused: Boolean) = getIcon(0)
    }

    override fun toDocString() = buildString {
        //Note: since this can only do lexer-based highlighting, this will be approximately the same style as
        //the editor display (but could have some keyword highlighting issues in the body)
        HtmlSyntaxInfoUtil.appendStyledSpan(
            this, SmithyColorSettings.TRAIT_NAME, "@$shapeName", 1f
        )
        body?.let { body ->
            //Note: since keys are dynamically highlighted using an annotator, this will manually apply the same
            //behavior to improve readability of trait values
            if (body.entries.isNotEmpty()) {
                append(body.entries.joinToString(", ", "(", ")") {
                    val key = HtmlSyntaxInfoUtil.getStyledSpan(
                        SmithyColorSettings.KEY, it.key.text, 1f
                    )
                    val value = getHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                        project, SmithyLanguage, it.value.text, 1f
                    )
                    "$key: $value"
                })
            } else {
                appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
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
    return setName(element, element.nameIdentifier, newName)
}

private fun <T : PsiNamedElement> setName(element: T, nameIdentifier: PsiElement, newName: String?): T {
    val textRange = nameIdentifier.textRange
    val document = FileDocumentManager.getInstance().getDocument(element.containingFile.virtualFile)
    document!!.replaceString(textRange.startOffset, textRange.endOffset, newName!!)
    PsiDocumentManager.getInstance(element.project).commitDocument(document)
    return element
}