package io.github.hadixlin.springfoxplus

import io.github.hadixlin.springfoxplus.javadoc.DocStore
import org.apache.commons.lang3.StringUtils.removeAll
import org.apache.commons.lang3.reflect.FieldUtils
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.RequestHandler
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.swagger.common.SwaggerPluginSupport
import springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER

@Component
@Order(SWAGGER_PLUGIN_ORDER + 100)
class StaticDocOperationSummaryAndNotesReader(private val docStore: DocStore) : OperationBuilderPlugin {
	override fun apply(context: OperationContext) {
		val (typeName, methodName) = getDefinitionNames(context)
		val methodDescription = docStore.readMethodDescription(typeName, methodName)
		val description = methodDescription?.description?.split(delimiters = *arrayOf("\n"), ignoreCase = true, limit = 2)
		val summary = description?.getOrNull(0)?.removeHtmlTag()?.trim()
		val notes = description?.getOrNull(1)?.removeHtmlTag()?.trim()
		val operationBuilder = context.operationBuilder()
		if (summary != null) {
			operationBuilder.summary(summary)
		}
		if (notes != null) {
			operationBuilder.notes(notes)
		}
	}

	@Suppress("DEPRECATION")
	private fun getDefinitionNames(operationContext: OperationContext): Pair<String, String> {
		val requestContext = FieldUtils.readField(operationContext, "requestContext", true)
		val requestHandler = FieldUtils.readField(requestContext, "handler", true) as RequestHandler
		val method = requestHandler.handlerMethod.method
		val methodName = method.name
		val typeName = method.declaringClass.name
		return Pair(typeName, methodName)
	}

	override fun supports(delimiter: DocumentationType?): Boolean {
		return SwaggerPluginSupport.pluginDoesApply(delimiter)
	}

	private fun String.removeHtmlTag(): String {
		return removeAll(this, "<.*?>")
	}
}
