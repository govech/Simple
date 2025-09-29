package com.framework.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 统一的API响应格式
 */
@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
    @SerialName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * 请求是否成功
     */
    val isSuccess: Boolean
        get() = code == 200 || code == 0

    /**
     * 获取数据，如果请求失败则抛出异常
     */
    fun getDataOrThrow(): T {
        return if (isSuccess) {
            data ?: throw IllegalStateException("Data is null")
        } else {
            throw ApiException(code, message)
        }
    }
}

/**
 * 分页响应数据
 */
@Serializable
data class PageResponse<T>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("total")
    val total: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("page_size")
    val pageSize: Int,
    @SerialName("has_next")
    val hasNext: Boolean
) {
    /**
     * 总页数
     */
    val totalPages: Int
        get() = if (pageSize > 0) (total + pageSize - 1) / pageSize else 0
}

/**
 * 空响应（用于删除、更新等操作）
 */
@Serializable
data object EmptyResponse

/**
 * API异常
 */
class ApiException(
    val code: Int,
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * 常用的响应码
 */
object ApiCode {
    const val SUCCESS = 200
    const val SUCCESS_ALT = 0
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val INTERNAL_SERVER_ERROR = 500
    const val SERVICE_UNAVAILABLE = 503
}
