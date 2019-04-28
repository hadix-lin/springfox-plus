package io.github.hadixlin.springfoxplus

import springfox.documentation.swagger.common.SwaggerPluginSupport

const val AFTER_SWAGGER = SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 2000
const val BEFORE_SWAGGER = SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER - 100
