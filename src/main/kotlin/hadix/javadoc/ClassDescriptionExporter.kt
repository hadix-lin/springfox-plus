package hadix.javadoc

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.nio.file.Paths

val objectMapper = ObjectMapper().setDefaultPropertyInclusion(NON_EMPTY)!!

fun findStaticDoc(typeName: String, classLoader: ClassLoader): ClassDescription? {
    val dir = classLoader.getResource("/META-INF").file
    val inputFile = getOutputFile(typeName, dir)
    if (inputFile.exists()) {
        return null
    }
    return objectMapper.readValue(inputFile, ClassDescription::class.java)
}

private fun getOutputFile(typeName: String, outDir: String): File {
    val path = typeName.replace(Regex("\\."), "/")
    return File(outDir, "$path.json")
}

fun main(args: Array<String>) {
    val srcDir = if (args.isNotEmpty()) args[0] else "src/main/java"
    val outDir = if (args.size >= 2) args[1] else "target/classes/META-INF/static-doc"
    val javaSrcRoot = Paths.get(srcDir).toFile()
    val classDescriptions = mutableListOf<ClassDescription>()
    val srcFileVisitor = object : VoidVisitorAdapter<String>() {
        override fun visit(type: ClassOrInterfaceDeclaration, parentName: String?) {
            val description = type.toDescription(parentName)
            classDescriptions.add(description)
            super.visit(type, description.name)
        }
    }
    
    javaSrcRoot.walk()
            .filter { it.isFile }
            .filter { it.name.endsWith("java") }
            .forEach {
                val cu = JavaParser.parse(it)
                val parentName = cu.packageDeclaration.orElse(null)?.nameAsString
                cu.accept(srcFileVisitor, parentName)
            }

    classDescriptions.forEach { desc ->
        val typeName = desc.name
        val outputFile = getOutputFile(typeName, outDir)
        println(outputFile.absolutePath)
        outputFile.parentFile.mkdirs()
        objectMapper.writeValue(outputFile, desc)
    }
}

