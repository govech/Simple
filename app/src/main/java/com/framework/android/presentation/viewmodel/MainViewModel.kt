package com.framework.android.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.framework.android.base.BaseViewModel
import com.framework.android.data.local.datastore.UserPreferences
import com.framework.android.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 主ViewModel
 * 管理应用程序的全局状态
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : BaseViewModel() {
    
    // 主题模式
    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()
    
    // 用户登录状态
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    // 是否首次启动
    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()
    
    init {
        initializeApp()
    }
    
    /**
     * 初始化应用程序状态
     */
    private fun initializeApp() {
        viewModelScope.launch {
            // 获取主题模式
            userPreferences.getThemeMode().collect { mode ->
                _themeMode.value = mode
            }
        }
        
        viewModelScope.launch {
            // 获取登录状态
            authRepository.isLoggedIn().collect { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }
        
        viewModelScope.launch {
            // 检查是否首次启动
            userPreferences.isFirstLaunch().collect { firstLaunch ->
                _isFirstLaunch.value = firstLaunch
                if (firstLaunch) {
                    // 标记为非首次启动
                    userPreferences.setFirstLaunch(false)
                }
            }
        }
    }
    
    /**
     * 设置主题模式
     */
    fun setThemeMode(mode: String) {
        launchSafely {
            userPreferences.setThemeMode(mode)
        }
    }
    
    /**
     * 检查应用更新
     */
    fun checkForUpdates() {
        launchSafely {
            // TODO: 实现应用更新检查逻辑
        }
    }
    
    /**
     * 清理缓存
     */
    fun clearCache() {
        launchSafely(showLoading = true) {
            // TODO: 实现缓存清理逻辑
            showToast("缓存清理完成")
        }
    }
}
