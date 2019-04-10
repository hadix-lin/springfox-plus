package hadix.staticdoc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream
import java.io.OutputStream
import java.lang.ref.WeakReference

/**
 * 使用Jackson对ClassDescription对象进行序列化/反序列化然后写入到output或从input中读取.
 */
open class JsonDocStore(
		val input: (String) -> InputStream,
		val output: (String) -> OutputStream
) : DocStore {
	private val cache = mutableMapOf<WeakReference<String>, ClassDescription>()

	private val objectMapper = ObjectMapper()
			.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
			.registerKotlinModule()

	override fun read(typeName: String): ClassDescription? {
		val key = WeakReference(typeName)
		val cached = cache[key]
		if (cached != null) {
			return cached
		}
		val value = input(typeName).use { objectMapper.readValue(it, ClassDescription::class.java) }
		if (value != null) {
			cache[key] = value
		}
		return value
	}

	override fun write(desc: ClassDescription) {
		output(desc.name).use { objectMapper.writeValue(it, desc) }
	}

}