package io.github.hadixlin.springfoxplus

import io.github.hadixlin.springfoxplus.javadoc.DocStore
import org.apache.commons.lang3.reflect.FieldUtils.readField
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin
import springfox.documentation.spi.service.contexts.ParameterExpansionContext
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterMetadataAccessor
import springfox.documentation.swagger.common.SwaggerPluginSupport
import springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Member

@Suppress("UNCHECKED_CAST")
@Component
@Order(SWAGGER_PLUGIN_ORDER + 100)
class JavaDocExpandedParameterBuilder(private val docStore: DocStore) : ExpandedParameterBuilderPlugin {
    override fun apply(context: ParameterExpansionContext) {
        val metadataAccessor = readField(context, "metadataAccessor", true) as ModelAttributeParameterMetadataAccessor
        val annotatedElements = readField(metadataAccessor, "annotatedElements", true) as List<AnnotatedElement>
        val member = annotatedElements.first() as Member
        val typeName = member.declaringClass.name
        val description = docStore.readFieldDescription(typeName, context.fieldName)
        if (description != null) {
            context.parameterBuilder.description(description)
        }
    }

    override fun supports(delimiter: DocumentationType?): Boolean {
        return SwaggerPluginSupport.pluginDoesApply(delimiter)
    }
}