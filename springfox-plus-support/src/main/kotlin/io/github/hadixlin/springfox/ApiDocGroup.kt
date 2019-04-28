package io.github.hadixlin.springfox

/**
 * Api分组
 * @author hadix
 */
class ApiDocGroup(val name: String) {
    /**
     * 接口路径前缀
     */
    var pathPrefix: Array<String> = arrayOf()
    /**
     * 接口路径正则表达式
     */
    var pathPattern: String? = null
    /**
     * 文档信息
     */
    var apiDocInfo: ApiDocInfo? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiDocGroup

        if (name != other.name) return false
        if (!pathPrefix.contentEquals(other.pathPrefix)) return false
        if (pathPattern != other.pathPattern) return false
        if (apiDocInfo != other.apiDocInfo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + pathPrefix.contentHashCode()
        result = 31 * result + (pathPattern?.hashCode() ?: 0)
        result = 31 * result + (apiDocInfo?.hashCode() ?: 0)
        return result
    }
}

class ApiDocInfo {
    /**
     * 文档标题
     */
    var title: String? = null
    /**
     * 文档描述
     */
    var description: String? = null
    /**
     * 联系人
     */
    var contact: String? = null
    /**
     * 版本
     */
    var version: String? = null

}

