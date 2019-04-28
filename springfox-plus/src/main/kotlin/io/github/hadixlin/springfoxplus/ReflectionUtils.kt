package io.github.hadixlin.springfoxplus

import org.apache.commons.lang3.reflect.FieldUtils

/** @author hadix
 */
object ReflectionUtils {
    fun <T> readField(target: Any, fieldName: String, fieldType: Class<T>): T {
        val value: Any
        try {
            value = FieldUtils.readField(target, fieldName, true)
        } catch (e: IllegalAccessException) {
            throw IllegalArgumentException(e.message, e)
        }

        return fieldType.cast(value)
    }
}
