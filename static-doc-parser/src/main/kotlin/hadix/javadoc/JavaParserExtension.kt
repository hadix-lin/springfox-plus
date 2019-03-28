package hadix.javadoc

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.type.PrimitiveType.Primitive.BOOLEAN
import hadix.staticdoc.ClassDescription
import hadix.staticdoc.MethodDescription
import hadix.staticdoc.MethodType
import hadix.staticdoc.MethodType.*


fun ClassOrInterfaceDeclaration.toDescription(parentName: String?): ClassDescription {
    val classDesc = this.javadoc.orElse(null)?.description?.toText()
    val fields: Map<String, String> = getFields(this)
    val methods = getMethodDescription(this)
    val properties = getProperties(methods)
    return ClassDescription(getTypeName(this, parentName), classDesc, fields, methods, properties)
}

private fun getTypeName(type: ClassOrInterfaceDeclaration, parentName: String?): String {
    return if (parentName != null) {
        parentName + (if (type.isNestedType && type.isStatic) "$" else ".") + type.nameAsString
    } else {
        type.nameAsString
    }
}

private fun getFields(type: TypeDeclaration<*>): Map<String, String> {
    return type.fields.filter { fd -> fd.hasJavaDocComment() }
            .flatMap { fd ->
                fd.variables.map { v -> Pair(v.nameAsString, fd.javadoc.get().description.toText()) }
            }
            .toMap()
}

private fun getMethodDescription(type: TypeDeclaration<*>): Map<String, MethodDescription> {
    return type.methods
            .filter { m -> m.hasJavaDocComment() }
            .associateBy(
                    { m -> m.nameAsString },
                    { m ->
                        val javadoc = m.javadoc.get()
                        val methodDesc = javadoc.description.toText()
                        val parameters = javadoc.blockTags
                                .filter { it.tagName == "param" && it.name.isPresent }
                                .map { Pair(it.name.get(), it.content.toText()) }
                                .toMap()
                        val returnDoc = javadoc.blockTags.find { it.tagName == "return" }?.content?.toText()
                        MethodDescription(methodDesc, m.methodType(), parameters, returnDoc)
                    })
}

private fun getProperties(methods: Map<String, MethodDescription>): MutableMap<String, String> {
    val properties = mutableMapOf<String, String>()
    methods.filter { (_, m) -> m.methodType == GETTER || m.methodType == SETTER }
            .forEach { (name, m) ->
                val description = m.description ?: return@forEach
                when (m.methodType) {
                    GETTER -> properties[name.propName()] = description
                    SETTER -> properties.putIfAbsent(name.propName(), description)
                    else -> Unit
                }
            }
    return properties
}

private fun MethodDeclaration.methodType(): MethodType {
    val params = this.parameters
    val name = this.nameAsString
    return when (params.size) {
        0 -> when {
            name.startsWith("get")
                    || (name.startsWith("is")
                    && this.type.isPrimitiveType
                    && this.type.asPrimitiveType().type == BOOLEAN) -> GETTER
            else -> NORMAL
        }
        1 -> when {
            name.startsWith("set") -> SETTER
            else -> NORMAL
        }
        else -> NORMAL
    }
}

private fun String.propName(): String {
    var propName = this.removePrefix("get")
    if (propName.length < this.length) {
        return propName
    }
    propName = this.removePrefix("is")
    if (propName.length < this.length) {
        return propName
    }
    propName = this.removePrefix("set")
    if (propName.length < this.length) {
        return propName
    }
    return propName
}
