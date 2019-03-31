package hadix.javadoc

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import hadix.staticdoc.ClassDescription
import hadix.staticdoc.DiskDocStore
import java.io.File
import java.nio.file.Paths

const val DEFAULT_OUTPUT_DIR = "target/classes/META-INF/static-doc"
const val DEFAULT_SOURCE_DIR = "src/main/java"

/**
 * 解析指定目录中的java源文件为ClassDescription,并序列化保存到指定的目录中
 * @param javaSrcRoot java源文件根目录
 * @param outputDir 输出目录
 */
fun parse(javaSrcRoot: File, outputDir: File) {
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

    val docStore = DiskDocStore(outputDir)
    classDescriptions.forEach { desc -> docStore.save(desc) }
}

fun main(args: Array<String>) {
    val srcDir = if (args.isNotEmpty()) args[0] else DEFAULT_SOURCE_DIR
    val outDir = if (args.size >= 2) args[1] else DEFAULT_OUTPUT_DIR
    val javaSrcRoot = Paths.get(srcDir).toFile()
    parse(javaSrcRoot, File(outDir))
}

