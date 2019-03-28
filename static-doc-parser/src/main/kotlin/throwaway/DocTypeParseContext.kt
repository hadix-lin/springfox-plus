package throwaway

import com.github.javaparser.ast.body.TypeDeclaration
import lombok.Data

/**
 * @author hadix.ly
 * @date 2018-11-27
 */
@Data
open class DocTypeParseContext protected constructor() {
    var type: TypeDeclaration<*>? = null

    companion object {

        fun of(type: TypeDeclaration<*>): DocTypeParseContext {
            val ctx = DocTypeParseContext()
            ctx.type = type
            return ctx
        }
    }
}
