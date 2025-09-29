package com.framework.android.data.remote.interceptor

import com.framework.android.data.local.datastore.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证拦截器
 * 自动在请求头中添加认证token
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 获取token
        val token = runBlocking {
            userPreferences.getAccessToken().first()
        }
        
        // 如果没有token或者是登录/注册接口，直接执行原始请求
        if (token.isNullOrEmpty() || isAuthUrl(originalRequest.url.toString())) {
            return chain.proceed(originalRequest)
        }
        
        // 添加Authorization头
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
            
        return chain.proceed(authenticatedRequest)
    }
    
    /**
     * 判断是否为认证相关的URL（不需要添加token）
     */
    private fun isAuthUrl(url: String): Boolean {
        val authUrls = listOf(
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/auth/forgot-password"
        )
        return authUrls.any { url.contains(it) }
    }
}
