package io.github.hadixlin.springfoxplus

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("springfox-plus")
class SpringfoxPlusProperties {
    /** 是否开启接swagger文档  */
    var enable = true
}
