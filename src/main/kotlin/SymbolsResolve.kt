//import com.github.javaparser.ast.AccessSpecifier.DEFAULT
//import com.github.javaparser.ast.AccessSpecifier.PUBLIC
//import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration
//import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
//import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver
//import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver
//import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver
//import java.util.*
//
//fun main() {
//    val jarTypeSolvers = System.getProperties().getProperty("java.class.path")
//            .split(":")
//            .filter { !it.contains("jre") and it.endsWith("jar") }
//            .map { JarTypeSolver(it) }
//            .toTypedArray()
////    System.getProperties().forEach { println("${it.key}=${it.value}") }
//    val reflectionTypeSolver = ReflectionTypeSolver()
//    val javaParserTypeSolver = JavaParserTypeSolver("src/main/java")
//
//    val typeResolver = CombinedTypeSolver(javaParserTypeSolver, reflectionTypeSolver, *jarTypeSolvers)
//    val type = typeResolver.solveType("hadix.javadoc.JavaInterface")
//    type.allMethods
//            .filter {
//                (type.isInterface && it.declaration.accessSpecifier() in EnumSet.of(PUBLIC, DEFAULT))
//                        || (type.isClass && it.declaration.accessSpecifier() == PUBLIC)
//            }
//            .forEach {
//                println(it)
//                val methodDeclaration = it.declaration
//                methodDeclaration as JavaParserMethodDeclaration
//                val javaDocTag =
//                        methodDeclaration.wrappedNode
//                                .javadoc.get().blockTags
//                                .map { t -> Pair(t.name.orElse(t.tagName), t.content.toText()) }.toMap()
//                println(javaDocTag)
//                println(methodDeclaration.getParam(0).name)
//            }
////    val symbolResolver = JavaSymbolSolver(typeResolver)
////    JavaParser.getStaticConfiguration().setSymbolResolver(symbolResolver)
////    val cu = JavaParser.parse(Paths.get("src/main/java/hadix.javadoc.JavaInterface.java"))
////
////    cu.findAll(ClassOrInterfaceDeclaration::class.java)
////            .forEach { print(it) }
//    // 查找所有符合条件的类@ApiModel,@Api,@Controller,@RestController,@HsfProvider
//    // @ApiModel 生成springfox.documentation.schema.Model
//    // @Api,@Controller,@RestController,@HsfProvider 生成ApiDescription
//}