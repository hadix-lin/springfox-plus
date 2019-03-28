package throwaway

import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.TypeDeclaration

/**
 * @author hadix.ly
 * @date 2018-11-27
 */
class DocMethodParseContext protected constructor() : DocTypeParseContext() {
    var method: MethodDeclaration? = null
        set(method) {
            field = this.method
        }

    companion object {

        fun of(type: TypeDeclaration<*>, method: MethodDeclaration): DocMethodParseContext {
            val ctx = DocMethodParseContext()
            ctx.type = type
            ctx.method = method
            return ctx
        }
    }
}
