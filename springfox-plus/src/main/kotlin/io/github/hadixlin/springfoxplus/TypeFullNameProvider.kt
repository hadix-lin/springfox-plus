package io.github.hadixlin.springfoxplus

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.schema.DefaultTypeNameProvider

/** @author hadix.ly
 */
@Component
@Order(BEFORE_SWAGGER)
class TypeFullNameProvider : DefaultTypeNameProvider() {
    override fun nameFor(type: Class<*>): String {
        return type.canonicalName
    }
}
