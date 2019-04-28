package io.github.hadixlin.springfoxplus.external

import io.github.hadixlin.springfoxplus.AFTER_SWAGGER
import io.github.hadixlin.springfoxplus.ReflectionUtils.readField
import io.swagger.annotations.ExternalDocs
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.swagger.common.SwaggerPluginSupport

/** @author hadix
 */
@Component
@Order(AFTER_SWAGGER)
class CustomApiExternalDocsOperationBuilder : OperationBuilderPlugin {

    override fun apply(context: OperationContext) {
        val externalDocsAnnotation = context.findAnnotation(ExternalDocs::class.java)
        if (externalDocsAnnotation.isPresent) {
            val externalDocs = externalDocsAnnotation.get()
            val operationBuilder = context.operationBuilder()
            val notes = readField(operationBuilder, "notes", String::class.java)
            val docName = externalDocs.value
            val docUrl = externalDocs.url
            val newNotes = createExternalDocsDescription(notes, docName, docUrl)
            operationBuilder.notes(newNotes)
        }
    }

    override fun supports(delimiter: DocumentationType): Boolean {
        return SwaggerPluginSupport.pluginDoesApply(delimiter)
    }
}
