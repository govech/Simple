package com.framework.android.data.remote.interceptor

import com.framework.android.BuildConfig
import com.framework.android.utils.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 日志拦截器工厂
 * 根据构建配置决定日志级别
 */
@Singleton
class LoggingInterceptor @Inject constructor() {
    
    fun create(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Logger.d("HTTP", message)
        }.apply {
            level = if (BuildConfig.ENABLE_LOGGING) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}
