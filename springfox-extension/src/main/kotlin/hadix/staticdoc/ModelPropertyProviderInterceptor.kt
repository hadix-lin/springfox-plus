package hadix.staticdoc

import com.fasterxml.classmate.ResolvedType
import org.apache.commons.lang3.reflect.FieldUtils.writeField
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import springfox.documentation.builders.ModelPropertyBuilder
import springfox.documentation.schema.ModelProperty

@Suppress("UNCHECKED_CAST", "unused", "UNUSED_PARAMETER")
@Component
@Aspect
class ModelPropertyProviderInterceptor(@Autowired private val docStore: DocStore) {

	@Pointcut("execution(* springfox.documentation.schema.property.OptimizedModelPropertiesProvider.propertiesFor(..)) && args(type,..)")
	private fun propertiesFor(type: ResolvedType) {
	}

	@Around(value = "propertiesFor(type)", argNames = "pjp,type")
	fun staticDocModelPropertyFor(pjp: ProceedingJoinPoint, type: ResolvedType): Any? {
		val properties = pjp.proceed()!! as List<ModelProperty>
		val staticDoc = docStore.read(type.typeName)
		return properties.map { it.fillStaticDoc(staticDoc) }.toList()
	}

	private fun ModelProperty.fillStaticDoc(staticDoc: ClassDescription?): ModelProperty? {
		val propDesc = staticDoc?.properties?.get(this.name)
				?: staticDoc?.fields?.get(this.name)
		return when {
			propDesc != null -> {
				val newProp = newBuilder(this).description(propDesc).build()
				writeField(newProp, "modelRef", this.modelRef, true)
				newProp
			}
			else -> this
		}
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