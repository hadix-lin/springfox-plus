package hadix.staticdoc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class DiskDocStore(private val storeDir: File) : DocStore {
    private val objectMapper = ObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)!!

    override fun find(typeName: String): ClassDescription? {
        val inputFile = getOutputFile(typeName)
        if (inputFile.exists()) {
            return null
        }
        return objectMapper.readValue(inputFile, ClassDescription::class.java)
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


