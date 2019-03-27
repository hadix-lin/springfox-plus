package hadix.staticdoc

import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.schema.contexts.ModelPropertyContext
import springfox.documentation.swagger.common.SwaggerPluginSupport

class StaticDocModelPropertyBuilder : ModelPropertyBuilderPlugin {
    override fun apply(context: ModelPropertyContext) {
        context.beanPropertyDefinition.get()
        context.annotatedElement.get()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun supports(delimiter: DocumentationType?): Boolean {
        return SwaggerPluginSupport.pluginDoesApply(delimiter)
    }
}