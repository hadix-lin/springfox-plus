package io.github.hadixlin.springfoxplus

import io.github.hadixlin.springfoxplus.javadoc.ClasspathDocStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StaticDocAutoConfiguration {

	@Bean
	fun docStore(): ClasspathDocStore {
		return ClasspathDocStore(DEFAULT_CLASSPATH_PARENT)
	}

	companion object {
		const val DEFAULT_CLASSPATH_PARENT = "/META-INF/static-doc"
	}
}