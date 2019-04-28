package io.github.hadixlin.springfoxplus

import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration::class)
@ConditionalOnProperty(
    value = ["springfox-plus.enable"],
    havingValue = "true",
    matchIfMissing = true
)
class SpringfoxPlusAutoConfiguration {

    @Bean
    fun springfoxPlusConfiguration() = SpringfoxPlusConfiguration()
}