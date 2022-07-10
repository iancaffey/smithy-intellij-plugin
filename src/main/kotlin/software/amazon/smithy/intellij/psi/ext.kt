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
import software.amazon.smithy.intellij.psi.impl.SmithyAggregateShapeImpl
import software.amazon.smithy.intellij.psi.impl.SmithyKeyedElementImpl
import software.amazon.smithy.intellij.psi.impl.SmithyPrimitiveImpl
import software.amazon.smithy.intellij.psi.impl.SmithyShapeImpl
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

interface SmithyBooleanExt : SmithyElement {
    fun booleanValue(): Boolean
}

abstract class SmithyBooleanMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyBoolean {
    override fun booleanValue() = text.toBoolean()
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
}

abstract class SmithyKeyMixin(node: ASTNode) : SmithyPsiElement(node), SmithyKey {
    override val reference by lazy { SmithyKeyReference(this) }
}

interface SmithyKeyedElementExt : SmithyNamedElement {
    val nameIdentifier: SmithyKey
    override fun getName(): String
}

abstract class SmithyKeyedElementMixin(node: ASTNode) : SmithyPsiElement(node), SmithyKeyedElement {
    override val nameIdentifier get() = key
    override fun getName(): String = key.text
    override fun setName(newName: String) = setName<SmithyKeyedElement>(this, newName)
    override fun getTextOffset() = key.textOffset
}

abstract class SmithyMapMixin(node: ASTNode) : SmithyAggregateShapeImpl(node), SmithyMap {
    override fun getMember(name: String) = members.find { it.name == "value" }
}

interface SmithyMemberExt : SmithyNamedElement, SmithyMemberDefinition {
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
}

abstract class SmithyMemberMixin(node: ASTNode) : SmithyPsiElement(node), SmithyMember {
    override val targetShapeId get() = shapeId.id
    override val targetShapeName get() = shapeId.shapeName
    override val enclosingShape: SmithyAggregateShape get() = getParentOfType(this, SmithyAggregateShape::class.java)!!
    override fun getName(): String = nameIdentifier.text
    override fun setName(newName: String) = setName<SmithyMember>(this, newName)
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun resolve(): SmithyShapeDefinition? = shapeId.reference.resolve()
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = "$name: ${shapeId.id}"
        override fun getLocationString() = (parent.parent as SmithyShape).shapeId
        override fun getIcon(unused: Boolean) = getIcon(0)
    }
}

interface SmithyMemberIdExt : SmithyElement {
    val id: String
    val reference: SmithyMemberReference
}

abstract class SmithyMemberIdMixin(node: ASTNode) : SmithyPsiElement(node), SmithyMemberId {
    override val id get() = "${shapeId.id}$${memberName.text}"
    override val reference by lazy { SmithyMemberReference(this) }
}

interface SmithyMetadataExt : SmithyStatement
abstract class SmithyMetadataMixin(node: ASTNode) : SmithyKeyedElementImpl(node), SmithyMetadata

interface SmithyModelExt : SmithyElement {
    val namespace: String
    val shapes: List<SmithyShape>
}

abstract class SmithyModelMixin(node: ASTNode) : SmithyPsiElement(node), SmithyModel {
    override val namespace get() = getChildOfType(this, SmithyNamespace::class.java)!!.namespaceId.id
    override val shapes: List<SmithyShape> get() = getChildrenOfTypeAsList(this, SmithyShape::class.java)
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

interface SmithyNumberExt : SmithyElement {
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
    override fun bigDecimalValue() = BigDecimal(text)
}

interface SmithyShapeExt : SmithyNamedElement, SmithyShapeDefinition, SmithyStatement {
    override val type get() = super.type
    override val documentation: SmithyDocumentation?
    override val declaredTraits: List<SmithyTrait>
    val model: SmithyModel
}

abstract class SmithyShapeMixin(node: ASTNode) : SmithyPsiElement(node), SmithyShape {
    override val typeIdentifier
        get() = nameIdentifier.siblings(forward = false, withSelf = false).first {
            it !is PsiWhiteSpace && it !is PsiComment
        }
    override val model get() = parent as SmithyModel
    override val namespace get() = model.namespace
    override val shapeId get() = "${namespace}#${name}"
    override val members get() = emptyList<SmithyMember>()
    override val documentation get() = getChildOfType(this, SmithyDocumentation::class.java)
    override val declaredTraits: List<SmithyTrait> get() = getChildrenOfTypeAsList(this, SmithyTrait::class.java)
    override fun getMember(name: String): SmithyMember? = members.find { it.name == name }
    override fun getName(): String = nameIdentifier.text
    override fun setName(newName: String) = setName<SmithyShape>(this, newName)
    override fun getNameIdentifier() = getChildOfType(this, SmithyShapeName::class.java)!!
    override fun getTextOffset() = nameIdentifier.textOffset
    override fun getPresentation() = object : ItemPresentation {
        override fun getPresentableText(): String = name
        override fun getLocationString(): String = namespace
        override fun getIcon(unused: Boolean) = getIcon(0)
    }

    override fun hasTrait(id: String): Boolean = declaredTraits.any {
        if (it.shape.id == id) return@any true
        if (it.shape.declaredNamespace != null) return@any false
        val target = it.resolve()
        target is SmithyShapeDefinition && id == target.shapeId
    }
}

interface SmithyShapeIdExt : SmithyElement {
    val id: String
    val shapeName: String
    val declaredNamespace: String?
    val enclosingNamespace: String
}

abstract class SmithyShapeIdMixin(node: ASTNode) : SmithyPrimitiveImpl(node), SmithyShapeId {
    override val id
        get() = buildString {
            declaredNamespace?.let { append(it).append("#") }
            append(shapeName)
        }
    override val shapeName: String get() = getChildOfType(this, SmithyId::class.java)!!.text
    override val declaredNamespace: String?
        get() {
            namespaceId?.let { return it.id }
            val model = (containingFile as SmithyFile).model
            val imports = getChildrenOfTypeAsList(model, SmithyImport::class.java)
            for (i in imports) {
                val importedShapeId = i.shapeId
                val importedNamespaceId = importedShapeId.namespaceId
                if (importedNamespaceId != null && shapeName == importedShapeId.shapeName) {
                    return importedNamespaceId.id
                }
            }
            return null
        }
    override val enclosingNamespace get() = (containingFile as SmithyFile).model!!.namespace

}

interface SmithyTraitExt : SmithyElement, SmithyTraitDefinition {
    fun resolve(): SmithyShapeDefinition?
}

abstract class SmithyTraitMixin(node: ASTNode) : SmithyPsiElement(node), SmithyTrait {
    override val shapeId: String = shape.id
    override val shapeName: String = shape.shapeName
    override fun resolve() = shape.reference.resolve()
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

interface SmithyValueExt : SmithyElement {
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