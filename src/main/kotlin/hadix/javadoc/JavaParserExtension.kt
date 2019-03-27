package hadix.javadoc

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.TypeDeclaration


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
                        MethodDescription(methodDesc, parameters, returnDoc)
                    })
}

private fun getProperties(methods: Map<String, MethodDescription>): MutableMap<String, String> {
    val properties = mutableMapOf<String, String>()
    methods.filter { (name, _) ->
        name.isGetter() || name.isSetter()
    }.forEach { (name, desc) ->
        if (desc.description != null) {
            if (name.isGetter()) {
                properties[name.propName()] = desc.description
            } else if (name.isSetter()) {
                properties.putIfAbsent(name.propName(), desc.description)
            }
        }
    }
    return properties
}

private fun String.isGetter(): Boolean {
    return this.startsWith("get") || this.startsWith("is")
}

private fun String.isSetter(): Boolean {
    return this.startsWith("set")
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
