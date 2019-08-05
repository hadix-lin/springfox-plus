package io.github.hadixlin.springfoxplus

import com.google.common.base.Defaults.defaultValue
import com.google.common.collect.ImmutableList
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiParam
import org.apache.commons.lang3.ClassUtils.isPrimitiveWrapper
import org.apache.commons.lang3.ClassUtils.wrapperToPrimitive
import org.apache.commons.lang3.StringUtils.isBlank
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.service.ResolvedMethodParameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin
import springfox.documentation.spi.service.ParameterBuilderPlugin
import springfox.documentation.spi.service.contexts.ParameterContext
import springfox.documentation.spi.service.contexts.ParameterExpansionContext

/**
 * @author hadix
 * @date 06/02/2017
 */
@Component
@Order(AFTER_SWAGGER)
class CustomApiParameterBuilder : ParameterBuilderPlugin, ExpandedParameterBuilderPlugin {

    override fun apply(ctx: ParameterContext) {
        val parameterBuilder = ctx.parameterBuilder()
        val parameter = ctx.resolvedMethodParameter()

        setRequiredIfNeed(parameterBuilder, parameter)

        setExampleToDefaultIfBlank(parameterBuilder, parameter)
    }

    override fun supports(delimiter: DocumentationType): Boolean {
        return true
    }

    override fun apply(ctx: ParameterExpansionContext) {
        setRequired(ctx)

        setExampleToDefaultIfBlank(ctx)
    }

    companion object {

        /**
         * 通过设置默认值,避免后续出现example解析错误
         */
        private fun setExampleToDefaultIfBlank(
            pBuilder: ParameterBuilder,
            parameter: ResolvedMethodParameter
        ) {
            val apiParam = parameter.findAnnotation(ApiParam::class.java)
            if (apiParam.isPresent && isBlank(apiParam.get().example)) {
                setExampleToDefaultValue(pBuilder, parameter.parameterType.erasedType)
            }
        }

        private fun setExampleToDefaultValue(
            parameterBuilder: ParameterBuilder,
            pType: Class<*>
        ) {
            val type = if (isPrimitiveWrapper(pType))
                wrapperToPrimitive(pType) else pType
            parameterBuilder.scalarExample(defaultValue(type))
        }

        private fun setRequired(ctx: ParameterExpansionContext) {
            if (ANNOTATIONS_FOR_REQUIRED.any { an ->
                    ctx.findAnnotation(an).isPresent
                }) {
                ctx.parameterBuilder.required(true)
            }
        }

        private val ANNOTATIONS_FOR_REQUIRED = ImmutableList.of(
            org.hibernate.validator.constraints.NotEmpty::class.java,
            org.hibernate.validator.constraints.NotBlank::class.java,
            javax.validation.constraints.NotEmpty::class.java,
            javax.validation.constraints.NotBlank::class.java,
            javax.validation.constraints.NotNull::class.java,
            javax.validation.constraints.Min::class.java
        )

        private fun setRequiredIfNeed(
            parameterBuilder: ParameterBuilder,
            parameter: ResolvedMethodParameter
        ) {
            for (aClass in ANNOTATIONS_FOR_REQUIRED) {
                val annotation = parameter.findAnnotation(aClass)
                if (annotation.isPresent) {
                    parameterBuilder.required(true)
                    break
                }
            }
        }

        private fun setExampleToDefaultIfBlank(ctx: ParameterExpansionContext) {
            val apiModelProperty = ctx.findAnnotation(ApiModelProperty::class.java)
            if (apiModelProperty.isPresent && isBlank(apiModelProperty.get().example)) {
                setExampleToDefaultValue(ctx.parameterBuilder, ctx.fieldType.erasedType)
            }
        }
    }
}
