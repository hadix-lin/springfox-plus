package io.github.hadixlin.springfoxplus

import com.fasterxml.classmate.TypeResolver
import io.github.hadixlin.springfox.ApiDocGroup
import io.github.hadixlin.springfox.ApiDocInfo
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.schema.AlternateTypeRules
import springfox.documentation.schema.WildcardType
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.VendorExtension
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * @author hadix
 * @date 13/03/2017
 */
@Configuration
@ComponentScan
@Import(BeanValidatorPluginsConfiguration::class)
@EnableSwagger2
class SpringfoxPlusConfiguration : WebMvcConfigurerAdapter(), BeanPostProcessor {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
        registry
            .addResourceHandler("/swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/doc/swagger-ui.html")
        registry
            .addResourceHandler("/doc/**")
            .addResourceLocations("classpath:/META-INF/doc/")
        registry
            .addResourceHandler("/markdown.html")
            .addResourceLocations("classpath:/META-INF/markdown.html")
    }

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        return bean
    }

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean !is ApiDocGroup) {
            return bean
        }

        val docInfo = bean.apiDocInfo ?: defaultApiInfo()

        val apiInfo = ApiInfo(
            docInfo.title,
            docInfo.description,
            docInfo.version,
            "",
            Contact(docInfo.contact, "", ""),
            "",
            "",
            emptyList<VendorExtension<*>>()
        )

        val pathRegex: Regex? =
            when {
                bean.pathPattern != null -> Regex(bean.pathPattern as String)
                else -> null
            }
        val tr = TypeResolver()
        return Docket(DocumentationType.SWAGGER_2)
            .protocols(setOf("http"))
            .apiInfo(apiInfo)
            .groupName(bean.name)
            .useDefaultResponseMessages(false)
            .select()
            .paths { input ->
                when {
                    input == null -> false
                    pathRegex != null -> pathRegex.matches(input)
                    else -> bean.pathPrefix.any { input.startsWith(it) }
                }
            }
            .build()
            .alternateTypeRules(
                AlternateTypeRules.newRule(
                    tr.resolve(Collection::class.java, WildcardType::class.java),
                    tr.resolve(List::class.java, WildcardType::class.java)
                )
            )
    }

    private fun defaultApiInfo(): ApiDocInfo {
        val info = ApiDocInfo()
        info.title = "请填写API文档标题"
        info.contact = "请填写作者名称"
        return info
    }
}
