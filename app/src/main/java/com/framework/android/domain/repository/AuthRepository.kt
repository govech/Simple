package com.framework.android.domain.repository

import com.framework.android.base.Result
import com.framework.android.domain.model.AuthToken
import com.framework.android.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 认证Repository接口
 * 定义认证相关的数据操作
 */
interface AuthRepository {
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 认证结果，包含用户信息和令牌
     */
    suspend fun login(username: String, password: String): Result<Pair<User, AuthToken>>
    
    /**
     * 用户注册
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @return 认证结果，包含用户信息和令牌
     */
    suspend fun register(username: String, email: String, password: String): Result<Pair<User, AuthToken>>
    
    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return 新的令牌信息
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthToken>
    
    /**
     * 用户登出
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * 忘记密码
     * @param email 邮箱地址
     */
    suspend fun forgotPassword(email: String): Result<Unit>
    
    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Flow<Boolean>
    
    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(): Flow<User?>
    
    /**
     * 获取访问令牌
     */
    fun getAccessToken(): Flow<String?>
    
    /**
     * 保存用户信息和令牌
     */
    suspend fun saveUserSession(user: User, token: AuthToken)
    
    /**
     * 清除用户会话
     */
    suspend fun clearUserSession()
}
