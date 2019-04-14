package io.github.hadixlin.springfoxplus.javadoc

interface DocStore {
    /**
     * 查找指定类名的静态文档
     *
     * @param typeName 类型全名
     * @return 类静态描述对象
     */
    fun read(typeName: String): ClassDescription?

    /**
     * 保存类的静态描述文档
     *
     * @param desc 类静态描述文档
     */
    fun write(desc: ClassDescription)

    /**
     * 读取方法注释
     * @param typeName 类型全名
     * @param methodName 方法名
     * @return 方法静态描述对象
     */
    fun readMethodDescription(typeName: String, methodName: String): MethodDescription? {
        return read(typeName)?.methods?.get(methodName)
    }

    /**
     * 读取方法参数的注释
     * @param typeName 类型全名
     * @param methodName 方法名
     * @param paramName 参数名
     * @return 方法参数的注释
     */
    fun readMethodParameterDescription(
            typeName: String, methodName: String, paramName: String): String? {
        return readMethodDescription(typeName, methodName)?.parameters?.get(paramName)
    }

    /**
     * 读取字段或属性的注释
     * @param typeName 类型全名
     * @param paramName 字段名或属性名
     * @return  字段或属性的注释
     */
    fun readFieldDescription(typeName: String, paramName: String): String? {
        val classDescription = read(typeName) ?: return null
        return classDescription.properties[paramName] ?: classDescription.fields[paramName]
    }

}