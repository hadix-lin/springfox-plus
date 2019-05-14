package io.github.hadixlin.springfoxplus

import org.apache.commons.lang3.reflect.FieldUtils
import springfox.documentation.swagger.common.SwaggerPluginSupport

const val AFTER_SWAGGER = SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 2000
const val BEFORE_SWAGGER = SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER - 100
/**
 * @author hadix
 */
fun <T> Any.readField(fieldName: String, fieldType: Class<T>): T {
    val value = FieldUtils.readField(this, fieldName, true)
    return fieldType.cast(value)
}