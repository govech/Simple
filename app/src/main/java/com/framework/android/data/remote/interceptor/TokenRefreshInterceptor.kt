package com.framework.android.data.remote.interceptor

import com.framework.android.data.local.datastore.UserPreferences
import com.framework.android.data.remote.api.AuthService
import com.framework.android.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Token刷新拦截器
 * 当收到401错误时自动刷新token并重试请求
 */
@Singleton
class TokenRefreshInterceptor @Inject constructor(
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) : Interceptor {

    private val lock = Any()
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)
        
        // 如果响应码是401且不是认证接口，尝试刷新token
        if (response.code == 401 && !isAuthUrl(originalRequest.url.toString())) {
            synchronized(lock) {
                val refreshToken = runBlocking {
                    userPreferences.getRefreshToken().first()
                }
                
                if (!refreshToken.isNullOrEmpty()) {
                    try {
                        // 刷新token
                        val refreshResponse = runBlocking {
                            authService.refreshToken(RefreshTokenRequest(refreshToken))
                        }
                        
                        if (refreshResponse.isSuccessful) {
                            val tokenResponse = refreshResponse.body()
                            if (tokenResponse != null) {
                                // 保存新的token
                                runBlocking {
                                    userPreferences.saveTokens(
                                        accessToken = tokenResponse.accessToken,
                                        refreshToken = tokenResponse.refreshToken
                                    )
                                }
                                
                                // 使用新token重试原始请求
                                val newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                                    .build()
                                    
                                response.close()
                                return chain.proceed(newRequest)
                            }
                        }
                    } catch (e: Exception) {
                        // token刷新失败，清除用户数据
                        runBlocking {
                            userPreferences.clearUserData()
                        }
                    }
                }
            }
        }
        
        return response
    }
    
    /**
     * 判断是否为认证相关的URL
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
