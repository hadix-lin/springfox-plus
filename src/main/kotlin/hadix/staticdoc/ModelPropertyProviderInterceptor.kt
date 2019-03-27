package hadix.staticdoc

import com.fasterxml.classmate.ResolvedType
import hadix.javadoc.findStaticDoc
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import springfox.documentation.builders.ModelPropertyBuilder
import springfox.documentation.schema.ModelProperty

@Component
@Aspect
class ModelPropertyProviderInterceptor {

    @AfterReturning(
            "execution(* springfox.documentation.schema.property.OptimizedModelPropertiesProvider.propertiesFor(..)",
            argNames = "type,givenContext",
            returning = "properties")
    fun staticDocModelPropertyFor(type: ResolvedType, properties: List<ModelProperty>): List<ModelProperty> {

        val staticDoc = findStaticDoc(type.typeName, this.javaClass.classLoader)
        return properties.map { p ->
            var propDesc = staticDoc?.properties?.get(p.name)
            if (propDesc == null) {
                propDesc = staticDoc?.fields?.get(p.name)
            }
            when {
                propDesc != null -> newBuilder(p).description(propDesc).build()
                else -> p
            }
        }.toList()
    }

    private fun newBuilder(prop: ModelProperty): ModelPropertyBuilder {
        val builder = ModelPropertyBuilder()
        return builder.name(prop.name)
                .position(prop.position)
                .pattern(prop.pattern)
                .qualifiedType(prop.qualifiedType)
                .readOnly(prop.isReadOnly)
                .required(prop.isRequired)
                .type(prop.type)
                .xml(prop.xml)
                .allowableValues(prop.allowableValues)
                .allowEmptyValue(prop.isAllowEmptyValue)
                .defaultValue(prop.defaultValue)
                .description(prop.description)
                .example(prop.example)
                .extensions(prop.vendorExtensions)
                .isHidden(prop.isHidden)
    }
}