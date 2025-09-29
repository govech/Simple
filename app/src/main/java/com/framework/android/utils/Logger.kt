package com.framework.android.utils

import android.util.Log
import com.framework.android.BuildConfig

/**
 * 日志工具类
 * 统一管理应用的日志输出
 */
object Logger {

    private const val DEFAULT_TAG = "AndroidFramework"
    
    // 是否启用日志（在Release版本中自动禁用）
    private val isLoggingEnabled = BuildConfig.ENABLE_LOGGING

    /**
     * Debug级别日志
     */
    fun d(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.d(tag, message, throwable)
            } else {
                Log.d(tag, message)
            }
        }
    }

    /**
     * Info级别日志
     */
    fun i(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.i(tag, message, throwable)
            } else {
                Log.i(tag, message)
            }
        }
    }

    /**
     * Warning级别日志
     */
    fun w(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }

    /**
     * Error级别日志
     */
    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    /**
     * 记录方法调用
     */
    fun methodCall(tag: String = DEFAULT_TAG, methodName: String, params: Map<String, Any>? = null) {
        if (isLoggingEnabled) {
            val paramsStr = params?.entries?.joinToString(", ") { "${it.key}=${it.value}" } ?: ""
            d(tag, "[$methodName] called with params: $paramsStr")
        }
    }

    /**
     * 记录网络请求
     */
    fun networkRequest(url: String, method: String, params: String? = null) {
        if (isLoggingEnabled) {
            d("Network", "[$method] $url ${params?.let { "params: $it" } ?: ""}")
        }
    }

    /**
     * 记录网络响应
     */
    fun networkResponse(url: String, statusCode: Int, responseTime: Long) {
        if (isLoggingEnabled) {
            d("Network", "Response: $url - Status: $statusCode - Time: ${responseTime}ms")
        }
    }

    /**
     * 记录性能信息
     */
    fun performance(tag: String = DEFAULT_TAG, operation: String, duration: Long) {
        if (isLoggingEnabled) {
            i(tag, "Performance: $operation took ${duration}ms")
        }
    }

    /**
     * 记录用户行为
     */
    fun userAction(action: String, params: Map<String, Any>? = null) {
        if (isLoggingEnabled) {
            val paramsStr = params?.entries?.joinToString(", ") { "${it.key}=${it.value}" } ?: ""
            i("UserAction", "$action $paramsStr")
        }
    }
}
