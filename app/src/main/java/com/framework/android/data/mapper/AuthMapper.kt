package com.framework.android.data.mapper

import com.framework.android.data.remote.dto.AuthResponse
import com.framework.android.domain.model.AuthToken
import com.framework.android.domain.model.User

/**
 * 认证数据映射器
 * 负责认证相关数据模型之间的转换
 */
object AuthMapper {
    
    /**
     * 认证响应转换为用户和令牌
     */
    fun AuthResponse.toDomainModels(): Pair<User, AuthToken> {
        val user = user.toDomainModel()
        val authToken = AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            tokenType = "Bearer"
        )
        return Pair(user, authToken)
    }
    
    /**
     * 认证响应转换为令牌
     */
    fun AuthResponse.toAuthToken(): AuthToken {
        return AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            tokenType = "Bearer"
        )
    }
}
