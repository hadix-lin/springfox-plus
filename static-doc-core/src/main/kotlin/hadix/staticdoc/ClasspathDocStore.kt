package hadix.staticdoc

import java.io.InputStream

/**
 * 从classpath中读取静态文档信息.不支持写入.
 */
class ClasspathDocStore(parent: String)
	: JsonDocStore({ it.input(parent) }, { throw NotImplementedError("has no need") }) {

	override fun write(desc: ClassDescription) {
		throw NotImplementedError("has no need")
	}
}

private fun String.typeToClasspath(parent: String): String {
	val typePath = this.replace(Regex("\\."), "/")
	return "$parent/$typePath"
}

private fun String.input(parent: String): InputStream {
	val cp = this.typeToClasspath(parent)
	return this.javaClass.classLoader.getResourceAsStream(cp)
}
