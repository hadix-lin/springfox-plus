package io.github.hadixlin.springfoxplus

import io.github.hadixlin.springfoxplus.javadoc.DocStore
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
		val (typeName, methodName, parameterName) = getDefinitionNames(parameterContext)
		val parameterDescription = docStore.readMethodParameterDescription(typeName, methodName, parameterName)
		if (parameterDescription != null) {
			parameterContext.parameterBuilder().description(parameterDescription)
		}
	}

	@Suppress("DEPRECATION")
	private fun getDefinitionNames(parameterContext: ParameterContext): Triple<String, String, String> {
		val requestContext = readField(parameterContext.operationContext, "requestContext", true)
		val requestHandler = readField(requestContext, "handler", true) as RequestHandler
		val method = requestHandler.handlerMethod.method
		val methodName = method.name
		val typeName = method.declaringClass.name
		val resolvedMethodParameter = parameterContext.resolvedMethodParameter()
		val parameterName = resolvedMethodParameter.defaultName().or(resolvedMethodParameter.parameterIndex.toString())
		return Triple(typeName, methodName, parameterName)
	}

	override fun supports(delimiter: DocumentationType?): Boolean {
		return SwaggerPluginSupport.pluginDoesApply(delimiter)
	}
}