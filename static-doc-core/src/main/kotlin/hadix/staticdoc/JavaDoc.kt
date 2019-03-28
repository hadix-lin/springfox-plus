package hadix.staticdoc

import java.io.Serializable

data class ClassDescription(
        val name: String,
        val description: String?,
        val fields: Map<String, String>,
        val methods: Map<String, MethodDescription>,
        val properties: Map<String, String>)
    : Serializable

data class MethodDescription(
        val description: String?,
        val methodType: MethodType,
        val parameters: Map<String, String>,
        val returnDoc: String?)
    : Serializable

enum class MethodType {
    NORMAL, GETTER, SETTER
}
