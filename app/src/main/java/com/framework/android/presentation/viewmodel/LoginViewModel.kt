package com.framework.android.presentation.viewmodel

import com.framework.android.base.BaseViewModel
import com.framework.android.base.Result
import com.framework.android.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 登录ViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    /**
     * 执行登录
     */
    fun login(username: String, password: String) {
        // 清除之前的错误
        _uiState.value = _uiState.value.copy(
            usernameError = null,
            passwordError = null,
            isLoginSuccessful = false
        )
        
        // 验证输入
        if (!validateInput(username, password)) {
            return
        }
        
        launchSafely(showLoading = true) {
            val params = LoginUseCase.Params(username, password)
            val result = loginUseCase(params)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoginSuccessful = true)
                    showToast("登录成功")
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
     * 验证输入
     */
    private fun validateInput(username: String, password: String): Boolean {
        var hasError = false
        
        if (username.isBlank()) {
            _uiState.value = _uiState.value.copy(usernameError = "用户名不能为空")
            hasError = true
        }
        
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "密码不能为空")
            hasError = true
        } else if (password.length < 6) {
            _uiState.value = _uiState.value.copy(passwordError = "密码长度不能少于6位")
            hasError = true
        }
        
        return !hasError
    }
    
    /**
     * 登录UI状态
     */
    data class LoginUiState(
        val usernameError: String? = null,
        val passwordError: String? = null,
        val isLoginSuccessful: Boolean = false
    )
}
