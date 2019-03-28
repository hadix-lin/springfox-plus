package hadix.javadoc

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import hadix.staticdoc.ClassDescription
import hadix.staticdoc.DiskDocStore
import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val srcDir = if (args.isNotEmpty()) args[0] else "src/throwaway.main/java"
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

    val docStore = DiskDocStore(File(outDir))
    classDescriptions.forEach { desc -> docStore.save(desc) }
}

