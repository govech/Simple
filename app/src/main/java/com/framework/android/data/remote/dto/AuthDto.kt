package com.framework.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 认证相关的数据传输对象
 */

/**
 * 登录请求
 */
@Serializable
data class LoginRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)

/**
 * 注册请求
 */
@Serializable
data class RegisterRequest(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

/**
 * 刷新Token请求
 */
@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)

/**
 * 认证响应
 */
@Serializable
data class AuthResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("user")
    val user: UserDto
)

/**
 * 用户信息DTO
 */
@Serializable
data class UserDto(
    @SerialName("id")
    val id: Long,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatar")
    val avatar: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
