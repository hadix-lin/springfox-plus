package io.github.hadixlin.springfoxplus

import io.github.hadixlin.springfoxplus.javadoc.ClasspathDocStore
import io.github.hadixlin.springfoxplus.javadoc.DEFAULT_CLASSPATH_PARENT
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
class JavaDocAutoConfiguration {

	@Bean
	fun docStore(): ClasspathDocStore {
		return ClasspathDocStore(DEFAULT_CLASSPATH_PARENT)
	}

}