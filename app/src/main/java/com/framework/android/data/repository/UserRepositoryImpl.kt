package com.framework.android.data.repository

import com.framework.android.base.BaseRepository
import com.framework.android.base.CacheStrategy
import com.framework.android.base.Result
import com.framework.android.data.local.dao.UserDao
import com.framework.android.data.local.datastore.UserPreferences
import com.framework.android.data.mapper.UserMapper.toDomainModel
import com.framework.android.data.mapper.UserMapper.toDomainModelList
import com.framework.android.data.mapper.UserMapper.toEntityList
import com.framework.android.data.remote.api.UserService
import com.framework.android.domain.model.User
import com.framework.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户Repository实现
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) : BaseRepository(), UserRepository {
    
    override suspend fun getUserInfo(userId: Long, forceRefresh: Boolean): Result<User> {
        return if (forceRefresh) {
            // 强制从网络获取
            getNetworkUser(userId)
        } else {
            // 先尝试从本地获取，如果没有再从网络获取
            val localUser = safeDatabaseCall { userDao.getUserById(userId) }
            when (localUser) {
                is Result.Success -> {
                    if (localUser.data != null) {
                        Result.Success(localUser.data.toDomainModel())
                    } else {
                        getNetworkUser(userId)
                    }
                }
                is Result.Error -> getNetworkUser(userId)
                is Result.Loading -> Result.Loading
            }
        }
    }
    
    override suspend fun getCurrentUser(forceRefresh: Boolean): Result<User> {
        return safeApiCall {
            val response = userService.getCurrentUser()
            val userDto = response.getDataOrThrow()
            val user = userDto.toDomainModel()
            
            // 缓存到本地
            safeDatabaseCall { userDao.insertUser(user.toEntity()) }
            
            user
        }
    }
    
    override suspend fun getUsers(
        page: Int,
        pageSize: Int,
        keyword: String?,
        forceRefresh: Boolean
    ): Result<List<User>> {
        return if (forceRefresh || !hasLocalUserData()) {
            // 从网络获取
            safeApiCall {
                val response = userService.getUsers(page, pageSize, keyword)
                val pageResponse = response.getDataOrThrow()
                val users = pageResponse.items.toDomainModelList()
                
                // 缓存到本地
                if (page == 1) {
                    // 第一页时清除旧数据
                    safeDatabaseCall { userDao.deleteAllUsers() }
                }
                safeDatabaseCall { userDao.insertUsers(users.toEntityList()) }
                
                users
            }
        } else {
            // 从本地获取
            safeDatabaseCall {
                val offset = (page - 1) * pageSize
                userDao.getUsersPaged(pageSize, offset).toDomainModelList()
            }
        }
    }
    
    override suspend fun searchUsers(
        keyword: String,
        page: Int,
        pageSize: Int
    ): Result<List<User>> {
        return safeApiCall {
            val response = userService.searchUsers(keyword, page, pageSize)
            val pageResponse = response.getDataOrThrow()
            pageResponse.items.toDomainModelList()
        }
    }
    
    override fun observeUser(userId: Long): Flow<User?> {
        return userDao.getUserByIdFlow(userId).map { entity ->
            entity?.toDomainModel()
        }
    }
    
    override fun observeCurrentUser(): Flow<User?> {
        return userPreferences.isLoggedIn().map { isLoggedIn ->
            if (isLoggedIn) {
                val userId = userPreferences.getUserId().first()
                userId?.let { id ->
                    userDao.getUserById(id)?.toDomainModel()
                }
            } else null
        }
    }
    
    override fun observeLocalUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.toDomainModelList()
        }
    }
    
    override suspend fun cacheUser(user: User): Result<Unit> {
        return safeDatabaseCall {
            userDao.insertUser(user.toEntity())
        }
    }
    
    override suspend fun cacheUsers(users: List<User>): Result<Unit> {
        return safeDatabaseCall {
            userDao.insertUsers(users.toEntityList())
        }
    }
    
    override suspend fun clearUserCache(): Result<Unit> {
        return safeDatabaseCall {
            userDao.deleteAllUsers()
        }
    }
    
    override suspend fun hasLocalUserData(): Boolean {
        return safeDatabaseCall {
            userDao.getUserCount() > 0
        }.getDataOrNull() == true
    }
    
    /**
     * 从网络获取用户信息并缓存
     */
    private suspend fun getNetworkUser(userId: Long): Result<User> {
        return safeApiCall {
            val response = userService.getUserInfo(userId)
            val userDto = response.getDataOrThrow()
            val user = userDto.toDomainModel()
            
            // 缓存到本地
            safeDatabaseCall { userDao.insertUser(user.toEntity()) }
            
            user
        }
    }
}
