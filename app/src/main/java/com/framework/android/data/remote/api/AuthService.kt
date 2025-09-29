package com.framework.android.data.remote.api

import com.framework.android.data.remote.dto.AuthResponse
import com.framework.android.data.remote.dto.BaseResponse
import com.framework.android.data.remote.dto.LoginRequest
import com.framework.android.data.remote.dto.RefreshTokenRequest
import com.framework.android.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 认证相关的API服务
 */
interface AuthService {

    /**
     * 用户登录
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<AuthResponse>

    /**
     * 用户注册
     */
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse<AuthResponse>

    /**
     * 刷新访问令牌
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<AuthResponse>

    /**
     * 用户登出
     */
    @POST("auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    /**
     * 忘记密码
     */
    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body email: String
    ): BaseResponse<Unit>
}
