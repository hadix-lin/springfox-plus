package hadix.staticdoc

import org.apache.commons.lang3.reflect.FieldUtils.readField
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.RequestHandler
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.ParameterBuilderPlugin
import springfox.documentation.spi.service.contexts.ParameterContext
import springfox.documentation.swagger.common.SwaggerPluginSupport
import springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER

@Component
@Order(SWAGGER_PLUGIN_ORDER + 100)
class StaticDocParameterBuilder(private val docStore: DocStore) : ParameterBuilderPlugin {

    override fun apply(parameterContext: ParameterContext) {
        val (methodName, typeName, parameterName) = getDefinitionNames(parameterContext)
        val parameterDescription = docStore.readMethodParameterDescription(typeName, methodName, parameterName)
        if (parameterDescription != null) {
            parameterContext.parameterBuilder().description(parameterDescription)
        }
    }

    private fun getDefinitionNames(parameterContext: ParameterContext): Triple<String, String, String> {
        val requestContext = readField(parameterContext.operationContext, "requestContext", true)
        val requestHandler = readField(requestContext, "handler", true) as RequestHandler
        val methodName = requestHandler.handlerMethod.method.name
        val typeName = requestHandler.declaringClass().name
        val resolvedMethodParameter = parameterContext.resolvedMethodParameter()
        val parameterName = resolvedMethodParameter.defaultName().or(resolvedMethodParameter.parameterIndex.toString())
        return Triple(methodName, typeName, parameterName)
    }

    override fun supports(delimiter: DocumentationType?): Boolean {
        return SwaggerPluginSupport.pluginDoesApply(delimiter)
    }
}