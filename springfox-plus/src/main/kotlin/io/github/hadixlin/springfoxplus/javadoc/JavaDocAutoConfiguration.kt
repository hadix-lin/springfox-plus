package io.github.hadixlin.springfoxplus.javadoc

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