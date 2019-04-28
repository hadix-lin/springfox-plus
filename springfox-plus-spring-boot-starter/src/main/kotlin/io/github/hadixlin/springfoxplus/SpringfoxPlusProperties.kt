package io.github.hadixlin.springfoxplus

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author hadix.ly
 * @date 2018-11-23
 */
@Component
@ConfigurationProperties("springfox-plus")
@Data
class SpringfoxPlusProperties {
    /**
     * 启用swagger自动文档生成工具
     */
    internal var enable = true
}
