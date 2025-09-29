package com.framework.android.base

/**
 * 统一的结果封装类
 * 用于封装网络请求、数据库操作等可能失败的操作结果
 *
 * @param T 成功时返回的数据类型
 */
sealed class Result<out T> {
    /**
     * 成功状态
     * @param data 成功时返回的数据
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * 错误状态
     * @param exception 错误异常信息
     */
    data class Error(val exception: Throwable) : Result<Nothing>()

    /**
     * 加载中状态
     */
    data object Loading : Result<Nothing>()

    /**
     * 是否为成功状态
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * 是否为错误状态
     */
    val isError: Boolean
        get() = this is Error

    /**
     * 是否为加载中状态
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * 获取成功时的数据，如果不是成功状态则返回null
     */
    fun getDataOrNull(): T? {
        return if (this is Success) data else null
    }

    /**
     * 获取错误异常，如果不是错误状态则返回null
     */
    fun getExceptionOrNull(): Throwable? {
        return if (this is Error) exception else null
    }
}

/**
 * Result扩展函数
 */

/**
 * 当结果为成功时执行指定操作
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * 当结果为错误时执行指定操作
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

/**
 * 当结果为加载中时执行指定操作
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}

/**
 * 转换成功结果的数据类型
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(exception)
        is Result.Loading -> Result.Loading
    }
}

/**
 * 转换成功结果的数据类型（可能失败）
 */
inline fun <T, R> Result<T>.mapCatching(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> {
            try {
                Result.Success(transform(data))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
        is Result.Error -> Result.Error(exception)
        is Result.Loading -> Result.Loading
    }
}

/**
 * 获取数据或默认值
 */
fun <T> Result<T>.getOrDefault(defaultValue: T): T {
    return if (this is Result.Success) data else defaultValue
}

/**
 * 获取数据或执行默认操作
 */
inline fun <T> Result<T>.getOrElse(defaultValue: (Throwable?) -> T): T {
    return when (this) {
        is Result.Success -> data
        is Result.Error -> defaultValue(exception)
        is Result.Loading -> defaultValue(null)
    }
}

/**
 * 将可能抛出异常的操作包装为Result
 */
inline fun <T> resultOf(action: () -> T): Result<T> {
    return try {
        Result.Success(action())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
