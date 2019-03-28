package throwaway

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.javadoc.JavadocBlockTag
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.streams.toList

/**
 * @author hadix.ly
 * @date 2018-11-26
 */
object DocParse {
    fun parse(java: Path): Collection<ApiClass> {
        val cu: CompilationUnit = JavaParser.parse(java)
        return cu.types.stream()
                .filter { DocPredicates.isApiDeclaration(it) }
                .map { parse(it) }
                .toList()
    }

    private fun parse(type: TypeDeclaration<*>): ApiClass {
        val methods = type.methods
                .stream()
                .filter { DocPredicates.isApiOperation(it) }
                .map { parse(it) }
                .toList()
        return ApiClass(
                type.nameAsString,
                type.javadoc?.orElse(null)?.description?.toText() ?: "",
                methods)
    }

    private fun parse(method: MethodDeclaration): ApiMethod {
        val javadoc = method.javadoc.orElse(null)
        val tagMap = javadoc?.blockTags
                ?.filter { tag -> tag.type == JavadocBlockTag.Type.PARAM }
                ?.map { tag -> Pair(tag.name.orElse(""), tag.content.toText()) }
                ?.toMap()
                ?: Collections.emptyMap()
        val args = method.parameters.map { parse(it, tagMap[it.nameAsString]) }.toList()
        return ApiMethod(method.nameAsString, javadoc.description.toText(), args)
    }

    private fun parse(arg: Parameter, argJavaDoc: String?): ApiArgument {
        return ApiArgument(
                arg.name?.identifier ?: "",
                argJavaDoc ?: "",
                false)
    }

}

fun main() {
    //还要找到分组定义
    //定位源码文件目录
    val root = Paths.get("src/throwaway.main/java")
    Files.walk(root, FileVisitOption.FOLLOW_LINKS)
            .filter { Files.isRegularFile(it) }
            .filter { it.toFile().name.endsWith(".java") }
            .forEach { print(DocParse.parse(it)) }
    //递归扫描所有的*.java文件
    //找到所有的HsfProvider/Controller
    //找到符合条件的类
    //便利其中符合条件的方法
    //再定位其实现的接口或注解的serviceInterface属性
    //分析接口类和方法的javadoc
    //分析方法的返回值和参数中的对象生成Model
    //找到Model的定义类分析字段或getter/setter的javadoc
    //生成Swagger对象
}

