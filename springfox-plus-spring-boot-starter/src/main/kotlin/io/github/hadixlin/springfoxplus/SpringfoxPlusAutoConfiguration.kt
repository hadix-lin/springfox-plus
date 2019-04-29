package io.github.hadixlin.springfoxplus

import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration::class)
@ConditionalOnProperty(
    value = ["springfox-plus.enable"],
    havingValue = "true",
    matchIfMissing = true
)
@Import(SpringfoxPlusConfiguration::class)
@EnableConfigurationProperties(SpringfoxPlusProperties::class)
class SpringfoxPlusAutoConfiguration