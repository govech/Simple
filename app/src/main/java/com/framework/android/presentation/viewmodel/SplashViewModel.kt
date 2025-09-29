package com.framework.android.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.framework.android.base.BaseViewModel
import com.framework.android.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 启动屏ViewModel
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()
    
    init {
        checkAuthenticationStatus()
    }
    
    /**
     * 检查用户认证状态
     */
    private fun checkAuthenticationStatus() {
        launchSafely {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // 模拟启动屏显示时间
            delay(2000)
            
            // 检查用户是否已登录
            val isLoggedIn = authRepository.isLoggedIn().first()
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                navigationTarget = if (isLoggedIn) {
                    NavigationTarget.HOME
                } else {
                    NavigationTarget.LOGIN
                }
            )
        }
    }
    
    /**
     * 导航目标枚举
     */
    enum class NavigationTarget {
        NONE,
        LOGIN,
        HOME
    }
    
    /**
     * 启动屏UI状态
     */
    data class SplashUiState(
        val isLoading: Boolean = false,
        val navigationTarget: NavigationTarget = NavigationTarget.NONE
    )
}
