package com.framework.android.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Repository基类
 * 提供通用的数据获取、错误处理等功能
 */
abstract class BaseRepository {

    /**
     * 安全地执行网络请求
     * @param apiCall 网络请求的挂起函数
     * @return Result包装的结果
     */
    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(apiCall())
            } catch (throwable: Throwable) {
                Result.Error(handleApiException(throwable))
            }
        }
    }

    /**
     * 安全地执行数据库操作
     * @param databaseCall 数据库操作的挂起函数
     * @return Result包装的结果
     */
    protected suspend fun <T> safeDatabaseCall(
        databaseCall: suspend () -> T
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(databaseCall())
            } catch (throwable: Throwable) {
                Result.Error(handleDatabaseException(throwable))
            }
        }
    }

    /**
     * 创建Flow形式的网络请求
     * @param apiCall 网络请求的挂起函数
     * @return Flow<Result<T>>
     */
    protected fun <T> flowApiCall(
        apiCall: suspend () -> T
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)
        val result = safeApiCall(apiCall)
        emit(result)
    }.flowOn(Dispatchers.IO)

    /**
     * 创建带缓存策略的数据流
     * @param databaseCall 获取本地数据的函数
     * @param networkCall 获取网络数据的函数
     * @param shouldFetch 是否需要从网络获取数据的判断函数
     * @param saveFetchResult 保存网络数据到本地的函数
     * @return Flow<Result<T>>
     */
    protected fun <T> networkBoundResource(
        databaseCall: suspend () -> T,
        networkCall: suspend () -> T,
        shouldFetch: (T?) -> Boolean = { true },
        saveFetchResult: suspend (T) -> Unit = {}
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)

        // 首先从数据库获取数据
        val localData = safeDatabaseCall(databaseCall)
        if (localData is Result.Success) {
            emit(localData)

            // 判断是否需要从网络获取数据
            if (shouldFetch(localData.data)) {
                val networkResult = safeApiCall(networkCall)
                when (networkResult) {
                    is Result.Success -> {
                        // 保存网络数据到本地
                        safeDatabaseCall { saveFetchResult(networkResult.data) }
                        // 重新从数据库获取数据并发送
                        val updatedLocalData = safeDatabaseCall(databaseCall)
                        emit(updatedLocalData)
                    }
                    is Result.Error -> {
                        // 网络请求失败，但本地有数据，继续使用本地数据
                        emit(localData)
                    }
                    is Result.Loading -> {
                        // 不应该发生
                    }
                }
            }
        } else {
            // 本地数据获取失败，直接从网络获取
            val networkResult = safeApiCall(networkCall)
            when (networkResult) {
                is Result.Success -> {
                    safeDatabaseCall { saveFetchResult(networkResult.data) }
                    emit(networkResult)
                }
                is Result.Error -> {
                    emit(networkResult)
                }
                is Result.Loading -> {
                    // 不应该发生
                }
            }
        }
    }.catch { throwable ->
        emit(Result.Error(throwable))
    }.flowOn(Dispatchers.IO)

    /**
     * 处理API异常
     */
    private fun handleApiException(throwable: Throwable): Exception {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    400 -> BusinessException("请求参数错误")
                    401 -> BusinessException("未授权，请重新登录")
                    403 -> BusinessException("禁止访问")
                    404 -> BusinessException("请求的资源不存在")
                    408 -> NetworkException("请求超时")
                    500 -> ServerException("服务器内部错误")
                    502 -> ServerException("网关错误")
                    503 -> ServerException("服务不可用")
                    else -> ServerException("服务器错误：${throwable.code()}")
                }
            }
            is SocketTimeoutException -> NetworkException("连接超时")
            is UnknownHostException -> NetworkException("网络连接失败")
            is IOException -> NetworkException("网络连接异常")
            else -> Exception(throwable.message ?: "未知错误")
        }
    }

    /**
     * 处理数据库异常
     */
    private fun handleDatabaseException(throwable: Throwable): Exception {
        return Exception("数据库操作失败: ${throwable.message}")
    }
}

/**
 * 缓存策略枚举
 */
enum class CacheStrategy {
    /**
     * 仅使用缓存
     */
    CACHE_ONLY,

    /**
     * 仅使用网络
     */
    NETWORK_ONLY,

    /**
     * 先使用缓存，再更新网络数据
     */
    CACHE_FIRST,

    /**
     * 先使用网络，失败时使用缓存
     */
    NETWORK_FIRST
}
