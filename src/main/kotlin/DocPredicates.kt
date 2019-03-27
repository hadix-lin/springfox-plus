import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.TypeDeclaration

/**
 * @author hadix.ly
 * @date 2018-11-27
 */
object DocPredicates {

    fun isApiDeclaration(td: TypeDeclaration<*>): Boolean {
        return td.getAnnotationByName("Api").isPresent
    }

    fun isApiOperation(md: MethodDeclaration): Boolean {
        val parent = md.parentNode
        if (parent.isPresent) {
            val parentNode = parent.get()
            if (parentNode is ClassOrInterfaceDeclaration) {
                if (parentNode.isInterface) {
                    return true
                }
            }
        }

        return md.modifiers.contains(Modifier.PUBLIC)
    }
}
