package com.framework.android.domain.repository

import com.framework.android.base.Result
import com.framework.android.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 用户Repository接口
 * 定义用户相关的数据操作
 */
interface UserRepository {
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @param forceRefresh 是否强制从网络获取
     * @return 用户信息
     */
    suspend fun getUserInfo(userId: Long, forceRefresh: Boolean = false): Result<User>
    
    /**
     * 获取当前用户信息
     * @param forceRefresh 是否强制从网络获取
     */
    suspend fun getCurrentUser(forceRefresh: Boolean = false): Result<User>
    
    /**
     * 获取用户列表（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键词
     * @param forceRefresh 是否强制从网络获取
     */
    suspend fun getUsers(
        page: Int = 1,
        pageSize: Int = 20,
        keyword: String? = null,
        forceRefresh: Boolean = false
    ): Result<List<User>>
    
    /**
     * 搜索用户
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     */
    suspend fun searchUsers(
        keyword: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<User>>
    
    /**
     * 观察用户信息变化
     * @param userId 用户ID
     */
    fun observeUser(userId: Long): Flow<User?>
    
    /**
     * 观察当前用户信息变化
     */
    fun observeCurrentUser(): Flow<User?>
    
    /**
     * 观察本地用户列表
     */
    fun observeLocalUsers(): Flow<List<User>>
    
    /**
     * 缓存用户信息到本地
     */
    suspend fun cacheUser(user: User): Result<Unit>
    
    /**
     * 缓存用户列表到本地
     */
    suspend fun cacheUsers(users: List<User>): Result<Unit>
    
    /**
     * 清除本地用户缓存
     */
    suspend fun clearUserCache(): Result<Unit>
    
    /**
     * 检查本地是否有用户数据
     */
    suspend fun hasLocalUserData(): Boolean
}
