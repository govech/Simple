package com.framework.android.domain.model

/**
 * 认证令牌领域模型
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val tokenType: String = "Bearer"
) {
    /**
     * 检查访问令牌是否即将过期
     * @param thresholdMinutes 过期阈值（分钟），默认5分钟
     */
    fun isAccessTokenExpiringSoon(thresholdMinutes: Long = 5): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val expiryTime = currentTime + expiresIn
        val threshold = thresholdMinutes * 60
        
        return (expiryTime - currentTime) <= threshold
    }
    
    /**
     * 检查访问令牌是否已过期
     */
    fun isAccessTokenExpired(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        return currentTime >= expiresIn
    }
    
    /**
     * 获取完整的Authorization头值
     */
    fun getAuthorizationHeader(): String {
        return "$tokenType $accessToken"
    }
}
