package io.github.hadixlin.springfoxplus.external

import com.google.common.base.Optional
import io.github.hadixlin.springfoxplus.AFTER_SWAGGER
import io.github.hadixlin.springfoxplus.AnnotationHelper
import io.github.hadixlin.springfoxplus.ReflectionUtils.readField
import io.swagger.annotations.ExternalDocs
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.builders.ModelPropertyBuilder
import springfox.documentation.schema.Annotations.findPropertyAnnotation
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.schema.contexts.ModelPropertyContext
import springfox.documentation.swagger.common.SwaggerPluginSupport
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method

/**
 * @author hadix
 */
@Component
@Order(AFTER_SWAGGER)
class CustomApiModelExternalDocsPropertiesBuilder : ModelPropertyBuilderPlugin {

    override fun apply(context: ModelPropertyContext) {
        var externalDocs = Optional.absent<ExternalDocs>()

        if (context.annotatedElement.isPresent) {
            externalDocs = externalDocs.or(
                findExternalDocsAnnotation(context.annotatedElement.get())
            )
        }
        if (context.beanPropertyDefinition.isPresent) {
            externalDocs = externalDocs.or(
                findPropertyAnnotation(
                    context.beanPropertyDefinition.get(), ExternalDocs::class.java
                )
            )
        }
        if (externalDocs.isPresent) {
            setExternalDocs(context.builder, externalDocs.get())
        }
    }

    private fun setExternalDocs(mpBuilder: ModelPropertyBuilder, externalDocs: ExternalDocs) {
        val description = readField(mpBuilder, "description", String::class.java)
        val docUrl = externalDocs.url
        val docName = externalDocs.value
        val newDescription = createExternalDocsDescription(description, docName, docUrl)
        mpBuilder.description(newDescription)
    }

    private fun findExternalDocsAnnotation(annotated: AnnotatedElement): Optional<ExternalDocs> {
        var annotation = Optional.absent<ExternalDocs>()

        if (annotated is Method) {
            // If the annotated element is a method we can use this information to check
            // superclasses as well
            annotation = Optional.fromNullable(
                AnnotationHelper.findAnnotation(annotated, ExternalDocs::class.java)
            )
        }

        return annotation.or(
            Optional.fromNullable(
                AnnotationUtils.getAnnotation(annotated, ExternalDocs::class.java)
            )
        )
    }

    override fun supports(delimiter: DocumentationType): Boolean {
        return SwaggerPluginSupport.pluginDoesApply(delimiter)
    }

}
