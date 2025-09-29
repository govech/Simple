package com.framework.android.data.repository

import com.framework.android.base.BaseRepository
import com.framework.android.base.Result
import com.framework.android.data.local.datastore.UserPreferences
import com.framework.android.data.mapper.AuthMapper.toDomainModels
import com.framework.android.data.mapper.AuthMapper.toAuthToken
import com.framework.android.data.mapper.UserMapper.toDomainModel
import com.framework.android.data.remote.api.AuthService
import com.framework.android.data.remote.dto.LoginRequest
import com.framework.android.data.remote.dto.RefreshTokenRequest
import com.framework.android.data.remote.dto.RegisterRequest
import com.framework.android.domain.model.AuthToken
import com.framework.android.domain.model.User
import com.framework.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证Repository实现
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) : BaseRepository(), AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<Pair<User, AuthToken>> {
        return safeApiCall {
            val response = authService.login(LoginRequest(username, password))
            response.getDataOrThrow().toDomainModels()
        }
    }
    
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Pair<User, AuthToken>> {
        return safeApiCall {
            val response = authService.register(RegisterRequest(username, email, password))
            response.getDataOrThrow().toDomainModels()
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<AuthToken> {
        return safeApiCall {
            val response = authService.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                response.body()?.toAuthToken() 
                    ?: throw IllegalStateException("Empty response body")
            } else {
                throw IllegalStateException("Token refresh failed with code: ${response.code()}")
            }
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return safeApiCall {
            authService.logout()
            clearUserSession()
        }
    }
    
    override suspend fun forgotPassword(email: String): Result<Unit> {
        return safeApiCall {
            authService.forgotPassword(email)
        }
    }
    
    override fun isLoggedIn(): Flow<Boolean> {
        return userPreferences.isLoggedIn()
    }
    
    override fun getCurrentUser(): Flow<User?> {
        return userPreferences.isLoggedIn().map { isLoggedIn ->
            if (isLoggedIn) {
                val userId = userPreferences.getUserId().first()
                val username = userPreferences.getUsername().first()
                val email = userPreferences.getUserEmail().first()
                val avatar = userPreferences.getUserAvatar().first()
                
                if (userId != null && username != null && email != null) {
                    User(
                        id = userId,
                        username = username,
                        email = email,
                        avatar = avatar,
                        createdAt = java.util.Date(), // 这里应该从缓存或服务器获取真实时间
                        updatedAt = java.util.Date()
                    )
                } else null
            } else null
        }
    }
    
    override fun getAccessToken(): Flow<String?> {
        return userPreferences.getAccessToken()
    }
    
    override suspend fun saveUserSession(user: User, token: AuthToken) {
        // 保存令牌
        userPreferences.saveTokens(token.accessToken, token.refreshToken)
        
        // 保存用户信息
        userPreferences.saveUserInfo(
            userId = user.id,
            username = user.username,
            email = user.email,
            avatar = user.avatar
        )
    }
    
    override suspend fun clearUserSession() {
        userPreferences.clearUserData()
    }
}
