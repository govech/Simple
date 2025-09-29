package com.framework.android.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户偏好设置管理类
 * 使用DataStore存储用户相关的设置和状态
 */
@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    
    companion object {
        // 用户认证相关
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = longPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_AVATAR = stringPreferencesKey("user_avatar")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        
        // 应用设置
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val LANGUAGE = stringPreferencesKey("language")
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        private val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        
        // 缓存设置
        private val CACHE_SIZE = longPreferencesKey("cache_size")
        private val AUTO_SYNC = booleanPreferencesKey("auto_sync")
    }
    
    // 认证相关方法
    
    /**
     * 保存访问令牌和刷新令牌
     */
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[IS_LOGGED_IN] = true
        }
    }
    
    /**
     * 获取访问令牌
     */
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }
    
    /**
     * 获取刷新令牌
     */
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
    }
    
    /**
     * 保存用户信息
     */
    suspend fun saveUserInfo(
        userId: Long,
        username: String,
        email: String,
        avatar: String? = null
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USERNAME] = username
            preferences[USER_EMAIL] = email
            avatar?.let { preferences[USER_AVATAR] = it }
        }
    }
    
    /**
     * 获取用户ID
     */
    fun getUserId(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }
    
    /**
     * 获取用户名
     */
    fun getUsername(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USERNAME]
        }
    }
    
    /**
     * 获取用户邮箱
     */
    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }
    
    /**
     * 获取用户头像
     */
    fun getUserAvatar(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_AVATAR]
        }
    }
    
    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }
    
    /**
     * 清除用户数据（登出时调用）
     */
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(USER_ID)
            preferences.remove(USERNAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_AVATAR)
            preferences[IS_LOGGED_IN] = false
        }
    }
    
    // 应用设置相关方法
    
    /**
     * 设置主题模式
     */
    suspend fun setThemeMode(themeMode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }
    
    /**
     * 获取主题模式
     */
    fun getThemeMode(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_MODE] ?: "system"
        }
    }
    
    /**
     * 设置语言
     */
    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }
    
    /**
     * 获取语言设置
     */
    fun getLanguage(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[LANGUAGE] ?: "system"
        }
    }
    
    /**
     * 设置是否首次启动
     */
    suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = isFirstLaunch
        }
    }
    
    /**
     * 检查是否首次启动
     */
    fun isFirstLaunch(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_FIRST_LAUNCH] ?: true
        }
    }
    
    /**
     * 设置通知开关
     */
    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] = enabled
        }
    }
    
    /**
     * 获取通知开关状态
     */
    fun isNotificationEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[NOTIFICATION_ENABLED] ?: true
        }
    }
    
    /**
     * 设置缓存大小
     */
    suspend fun setCacheSize(size: Long) {
        context.dataStore.edit { preferences ->
            preferences[CACHE_SIZE] = size
        }
    }
    
    /**
     * 获取缓存大小
     */
    fun getCacheSize(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[CACHE_SIZE] ?: 100L * 1024 * 1024 // 默认100MB
        }
    }
    
    /**
     * 设置自动同步
     */
    suspend fun setAutoSync(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_SYNC] = enabled
        }
    }
    
    /**
     * 获取自动同步状态
     */
    fun isAutoSyncEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[AUTO_SYNC] ?: true
        }
    }
}
