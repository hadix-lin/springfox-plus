data class ApiClass(
        val name: String,
        val description: String?,
        val methods: Collection<ApiMethod>)

data class ApiMethod(
        val name: String,
        val description: String?,
        val args: Collection<ApiArgument>)

data class ApiArgument(
        val name: String,
        val description: String?,
        val required: Boolean)