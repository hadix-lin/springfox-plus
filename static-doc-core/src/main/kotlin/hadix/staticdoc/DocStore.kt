package hadix.staticdoc

interface DocStore {
    /**
     * 查找指定类名的静态文档
     *
     * @param typeName 类型全名
     * @return 类静态描述对象
     */
    fun find(typeName: String): ClassDescription?

    /**
     * 保存类的静态描述文档
     *
     * @param desc 类静态描述文档
     */
    fun save(desc: ClassDescription)
}