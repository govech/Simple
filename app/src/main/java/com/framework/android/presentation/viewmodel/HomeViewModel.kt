package com.framework.android.presentation.viewmodel

import com.framework.android.base.BaseViewModel
import com.framework.android.base.Result
import com.framework.android.domain.model.User
import com.framework.android.domain.repository.AuthRepository
import com.framework.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 主页ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadUserData()
    }
    
    /**
     * 加载用户数据
     */
    fun loadUserData() {
        launchSafely(showLoading = true) {
            val result = userRepository.getCurrentUser(forceRefresh = true)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(currentUser = result.data)
                }
                is Result.Error -> {
                    handleError(result.exception)
                }
                is Result.Loading -> {
                    // 加载状态由BaseViewModel处理
                }
            }
        }
    }
    
    /**
     * 用户登出
     */
    fun logout(onLogoutComplete: () -> Unit) {
        launchSafely(showLoading = true) {
            val result = authRepository.logout()
            
            when (result) {
                is Result.Success -> {
                    showToast("已退出登录")
                    onLogoutComplete()
                }
                is Result.Error -> {
                    // 即使登出失败，也清除本地会话
                    authRepository.clearUserSession()
                    onLogoutComplete()
                }
                is Result.Loading -> {
                    // 加载状态由BaseViewModel处理
                }
            }
        }
    }
    
    /**
     * 主页UI状态
     */
    data class HomeUiState(
        val currentUser: User? = null
    )
}
