package software.amazon.smithy.intellij.ext

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import software.amazon.smithy.intellij.SmithyFile
import software.amazon.smithy.intellij.SmithyShapeReference
import software.amazon.smithy.intellij.SmithyShapeReference.ByKey
import software.amazon.smithy.intellij.SmithyShapeReference.ByMember
import software.amazon.smithy.intellij.SmithyShapeReference.ByName
import software.amazon.smithy.intellij.psi.SmithyBoolean
import software.amazon.smithy.intellij.psi.SmithyDocumentation
import software.amazon.smithy.intellij.psi.SmithyEntry
import software.amazon.smithy.intellij.psi.SmithyId
import software.amazon.smithy.intellij.psi.SmithyImport
import software.amazon.smithy.intellij.psi.SmithyKey
import software.amazon.smithy.intellij.psi.SmithyKeyedElement
import software.amazon.smithy.intellij.psi.SmithyMember
import software.amazon.smithy.intellij.psi.SmithyMemberName
import software.amazon.smithy.intellij.psi.SmithyModel
import software.amazon.smithy.intellij.psi.SmithyNamedElement
import software.amazon.smithy.intellij.psi.SmithyNamespace
import software.amazon.smithy.intellij.psi.SmithyNamespaceId
import software.amazon.smithy.intellij.psi.SmithyNumber
import software.amazon.smithy.intellij.psi.SmithyShape
import software.amazon.smithy.intellij.psi.SmithyShapeId
import software.amazon.smithy.intellij.psi.SmithyShapeName
import software.amazon.smithy.intellij.psi.SmithySimpleShape
import software.amazon.smithy.intellij.psi.SmithySimpleTypeName
import software.amazon.smithy.intellij.psi.SmithyTrait
import software.amazon.smithy.intellij.psi.SmithyTypes
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * A utility class providing mixins for AST nodes (generated by [Grammar-Kit](https://github.com/JetBrains/Grammar-Kit)).
 *
 * All [SmithyNumber] value-conversion methods perform safe parsing (by using [BigDecimal]) but will fail
 * with an [ArithmeticException] if converting to an integral integer type and the number is too large to fit.
 *
 * @author Ian Caffey
 * @since 1.0
 */

private val DOCUMENTATION_LINES = TokenSet.create(SmithyTypes.TOKEN_DOCUMENTATION_LINE)
fun booleanValue(b: SmithyBoolean) = b.text.toBoolean()
fun byteValue(number: SmithyNumber) = bigDecimalValue(number).byteValueExact().toDouble()
fun shortValue(number: SmithyNumber) = bigDecimalValue(number).shortValueExact().toDouble()
fun intValue(number: SmithyNumber) = bigDecimalValue(number).intValueExact().toDouble()
fun floatValue(number: SmithyNumber) = bigDecimalValue(number).toFloat()
fun doubleValue(number: SmithyNumber) = bigDecimalValue(number).toDouble()
fun longValue(number: SmithyNumber) = bigDecimalValue(number).longValueExact()
fun bigDecimalValue(number: SmithyNumber) = BigDecimal(number.text)
fun bigIntegerValue(number: SmithyNumber): BigInteger = bigDecimalValue(number).toBigIntegerExact()
fun getOwner(documentation: SmithyDocumentation): PsiElement = documentation.parent
fun getTokenType(documentation: SmithyDocumentation): IElementType = SmithyTypes.DOCUMENTATION

//see: https://awslabs.github.io/smithy/1.0/spec/core/idl.html#documentation-comment
fun toDocString(documentation: SmithyDocumentation) =
    documentation.node.getChildren(DOCUMENTATION_LINES).joinToString("\n") { node ->
        node.text.let { it.substring(if (it.length > 3 && it[3] == ' ') 4 else 3) }
    }

fun toString(id: SmithyId): String = id.text
fun toString(name: SmithyMemberName): String = name.text
fun toString(id: SmithyShapeId): String {
    val builder = StringBuilder()
    val namespaceId = id.namespaceId
    if (namespaceId != null) {
        builder.append(namespaceId).append(".")
    }
    builder.append(id.shapeName)
    val memberName = id.memberName
    if (memberName != null) {
        builder.append("$").append(memberName)
    }
    return builder.toString()
}

fun toString(name: SmithyShapeName): String = name.text
fun toString(namespaceId: SmithyNamespaceId): String = namespaceId.parts.joinToString(".")
fun getName(element: SmithyKeyedElement): String = element.key.text
fun setName(element: SmithyKeyedElement, newName: String?) = setName<SmithyKeyedElement>(element, newName)
fun getNameIdentifier(element: SmithyKeyedElement): SmithyKey = element.key
fun getTextOffset(element: SmithyKeyedElement): Int = element.key.textOffset
fun getName(member: SmithyMember): String = member.nameIdentifier.text
fun setName(member: SmithyMember, newName: String?) = setName<SmithyMember>(member, newName)
fun getTextOffset(member: SmithyMember): Int = member.nameIdentifier.textOffset
fun getName(shape: SmithyShape): String = shape.nameIdentifier.text
fun setName(shape: SmithyShape, newName: String?) = setName<SmithyShape>(shape, newName)
fun getNameIdentifier(shape: SmithyShape): SmithyShapeName =
    PsiTreeUtil.getChildOfType(shape, SmithyShapeName::class.java)!!

fun getTextOffset(shape: SmithyShape) = shape.nameIdentifier.textOffset
fun getName(shapeId: SmithyShapeId): String = shapeId.nameIdentifier.text
fun setName(shapeId: SmithyShapeId, newName: String?) = setName<SmithyShapeId>(shapeId, newName)
fun getNameIdentifier(shapeId: SmithyShapeId): SmithyShapeName = shapeId.shapeName
fun getTextOffset(shapeId: SmithyShapeId) = shapeId.nameIdentifier.textOffset
fun getTypeName(shape: SmithySimpleShape): String =
    PsiTreeUtil.getChildOfType(shape, SmithySimpleTypeName::class.java)!!.text

fun getDocumentation(shape: SmithyShape): SmithyDocumentation? =
    PsiTreeUtil.getChildOfType(shape, SmithyDocumentation::class.java)

fun getNamespace(model: SmithyModel): String {
    val namespace = Objects.requireNonNull(PsiTreeUtil.getChildOfType(model, SmithyNamespace::class.java))
    return namespace!!.namespaceId.id
}

fun getNamespace(shape: SmithyShape) = getNamespace(shape.parent as SmithyModel)

fun getDeclaredNamespace(shapeId: SmithyShapeId): String? {
    val namespaceId = shapeId.namespaceId
    if (namespaceId != null) {
        return namespaceId.id
    }
    val model = (shapeId.containingFile as SmithyFile).model
    val imports = PsiTreeUtil.getChildrenOfTypeAsList(model, SmithyImport::class.java)
    for (i in imports) {
        val importedShapeId = i.shapeId
        val importedNamespaceId = importedShapeId.namespaceId
        if (importedNamespaceId != null && shapeId.name == importedShapeId.name) {
            return importedNamespaceId.id
        }
    }
    return null
}

fun getEnclosingNamespace(shapeId: SmithyShapeId): String {
    val model = (shapeId.containingFile as SmithyFile).model
    return PsiTreeUtil.getChildOfType(model, SmithyNamespace::class.java)!!.namespaceId.id
}

fun getDeclaredTraits(shape: SmithyShape): List<SmithyTrait> =
    PsiTreeUtil.getChildrenOfTypeAsList(shape, SmithyTrait::class.java)

fun getShapes(model: SmithyModel): List<SmithyShape> =
    PsiTreeUtil.getChildrenOfTypeAsList(model, SmithyShape::class.java)

fun getShapeId(shape: SmithyShape) = "${shape.namespace}#${shape.name}"
fun getId(id: SmithyNamespaceId) = id.parts.joinToString(".") { it.text }
fun getId(id: SmithyShapeId): String {
    val namespaceId = id.namespaceId
    val shapeName = id.shapeName
    val memberName = id.memberName
    val builder = StringBuilder()
    if (namespaceId != null) {
        builder.append(namespaceId.id).append("#")
    }
    builder.append(shapeName.text)
    if (memberName != null) {
        builder.append("$").append(memberName.text)
    }
    return builder.toString()
}

fun getPresentation(member: SmithyMember) = object : ItemPresentation {
    override fun getPresentableText(): String = member.name + ": " + member.shapeId.id
    override fun getLocationString() = (member.parent.parent as SmithyShape).shapeId
    override fun getIcon(unused: Boolean) = member.getIcon(0)
}

fun getPresentation(shape: SmithyShape) = object : ItemPresentation {
    override fun getPresentableText(): String = shape.name
    override fun getLocationString(): String = shape.namespace
    override fun getIcon(unused: Boolean) = shape.getIcon(0)
}

fun getReference(shapeId: SmithyShapeId) = SmithyShapeReference(shapeId)
fun getReference(shapeName: SmithyShapeName) = ByName(shapeName)
fun getReference(key: SmithyKey) = ByKey(key)
fun getReference(entry: SmithyEntry) = ByMember(entry)

private fun <T : SmithyNamedElement> setName(element: T, newName: String?): T {
    val name = element.nameIdentifier ?: return element
    val textRange = name.textRange
    val document = FileDocumentManager.getInstance().getDocument(name.containingFile.virtualFile)
    document!!.replaceString(textRange.startOffset, textRange.endOffset, newName!!)
    PsiDocumentManager.getInstance(name.project).commitDocument(document)
    return element
}