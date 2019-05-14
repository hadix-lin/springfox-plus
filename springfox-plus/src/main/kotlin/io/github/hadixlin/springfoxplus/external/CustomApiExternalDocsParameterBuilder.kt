package io.github.hadixlin.springfoxplus.external

import io.github.hadixlin.springfoxplus.AFTER_SWAGGER
import io.github.hadixlin.springfoxplus.readField
import io.swagger.annotations.ExternalDocs
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin
import springfox.documentation.spi.service.ParameterBuilderPlugin
import springfox.documentation.spi.service.contexts.ParameterContext
import springfox.documentation.spi.service.contexts.ParameterExpansionContext

/**
 * @author hadix
 */
@Component
@Order(AFTER_SWAGGER)
class CustomApiExternalDocsParameterBuilder : ParameterBuilderPlugin,
    ExpandedParameterBuilderPlugin {
    override fun apply(context: ParameterContext) {
        val parameter = context.resolvedMethodParameter()
        val parameterBuilder = context.parameterBuilder()

        val externalDocs = parameter.findAnnotation(ExternalDocs::class.java)
        if (externalDocs.isPresent) {
            appendExternalDocs(parameterBuilder, externalDocs.get())
        }
    }

    private fun appendExternalDocs(
        parameterBuilder: ParameterBuilder,
        externalDocsAnnotation: ExternalDocs
    ) {
        val docUrl = externalDocsAnnotation.url
        val docName = externalDocsAnnotation.value
        val description = parameterBuilder.readField("description", String::class.java)
        val newDescription = createExternalDocsDescription(description, docName, docUrl)
        parameterBuilder.description(newDescription)
    }

    override fun supports(delimiter: DocumentationType): Boolean {
        return true
    }

    override fun apply(context: ParameterExpansionContext) {
        setExternalDocs(context)
    }

    private fun setExternalDocs(ctx: ParameterExpansionContext) {
        val externalDocs = ctx.findAnnotation(ExternalDocs::class.java)
        if (externalDocs.isPresent) {
            appendExternalDocs(ctx.parameterBuilder, externalDocs.get())
        }
    }
}
