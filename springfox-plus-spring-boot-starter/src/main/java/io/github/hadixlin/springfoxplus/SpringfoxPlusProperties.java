package io.github.hadixlin.springfoxplus;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("springfox-plus")
public class SpringfoxPlusProperties {
    /** 是否开启接swagger文档 */
    private boolean enable = true;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
