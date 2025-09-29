package com.framework.android.domain.model

import java.util.Date

/**
 * 用户领域模型
 * 表示业务逻辑中的用户实体
 */
data class User(
    val id: Long,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val createdAt: Date,
    val updatedAt: Date
) {
    /**
     * 获取显示名称
     */
    fun getDisplayName(): String {
        return username.takeIf { it.isNotBlank() } ?: email
    }
    
    /**
     * 检查是否有头像
     */
    fun hasAvatar(): Boolean {
        return !avatar.isNullOrBlank()
    }
    
    /**
     * 获取头像URL或默认头像
     */
    fun getAvatarUrl(): String {
        return avatar ?: generateDefaultAvatar()
    }
    
    /**
     * 生成默认头像URL
     */
    private fun generateDefaultAvatar(): String {
        // 使用用户名首字母生成默认头像
        val initial = username.firstOrNull()?.uppercaseChar() ?: 'U'
        return "https://ui-avatars.com/api/?name=$initial&background=random"
    }
    
    /**
     * 检查邮箱是否有效
     */
    fun isEmailValid(): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
