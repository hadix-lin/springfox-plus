package hadix.staticdoc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.lang.ref.WeakReference

class DiskDocStore(private val storeDir: File) : DocStore {
    private val cache = mutableMapOf<WeakReference<String>, ClassDescription>()

    private val objectMapper = ObjectMapper()
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            .registerKotlinModule()

    override fun find(typeName: String): ClassDescription? {
        val key = WeakReference(typeName)
        val cached = cache[key]
        if (cached != null) {
            return cached
        }
        val inputFile = getOutputFile(typeName)
        if (!inputFile.exists()) {
            return null
        }
        val value = objectMapper.readValue(inputFile, ClassDescription::class.java)
        if (value != null) {
            cache[key] = value
        }
        return value
    }

    private fun getOutputFile(typeName: String): File {
        val path = typeName.replace(Regex("\\."), "/")
        return File(storeDir, "$path.json")
    }

    override fun save(desc: ClassDescription) {
        val typeName = desc.name
        val outputFile = getOutputFile(typeName)
        outputFile.parentFile.mkdirs()
        objectMapper.writeValue(outputFile, desc)
    }
}


