package hadix.staticdoc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StaticDocAutoConfiguration {

	@Bean
	fun docStore(): ClasspathDocStore {
		return ClasspathDocStore("/META-INF/static-doc")
	}
}