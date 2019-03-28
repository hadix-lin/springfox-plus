package throwaway

import io.swagger.annotations.Api

/**
 * @author hadix
 */
@Api(description = "任务开放接口", tags = ["throwaway.TaskApi"])
interface TaskApi {

    /**
     * 获取指定任务详情
     *
     * @param taskId 任务id
     */
    operator fun get(taskId: Long?): String

    /**
     * 查询任务
     *
     * @param query 查询条件
     */
    fun query(query: String): String

    /**
     * 保存草稿
     *
     * @param taskId 任务id
     * @param draft  草稿内容
     */
    fun saveArticleDraft(taskId: Long?, draft: String): String

    /**
     * 获取文章草稿
     *
     * @param draftId 草稿id
     */
    fun getArticleDraft(draftId: Long?): String

    /**
     * 领取任务
     *
     * @param taskId 任务id
     */
    fun take(taskId: Long?): String
}
