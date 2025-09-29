package com.framework.android.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel基类
 * 提供通用的错误处理、加载状态管理等功能
 */
abstract class BaseViewModel : ViewModel() {

    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 错误信息
    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    // Toast消息
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // 全局异常处理器
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    /**
     * 在ViewModel作用域中启动协程，自动处理异常
     */
    protected fun launchSafely(
        showLoading: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(exceptionHandler) {
            try {
                if (showLoading) setLoading(true)
                block()
            } finally {
                if (showLoading) setLoading(false)
            }
        }
    }

    /**
     * 设置加载状态
     */
    protected fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    /**
     * 处理错误
     */
    protected open fun handleError(throwable: Throwable) {
        setLoading(false)
        val errorMessage = when (throwable) {
            is NetworkException -> throwable.message ?: "网络错误"
            is ServerException -> throwable.message ?: "服务器错误"
            is BusinessException -> throwable.message ?: "业务错误"
            else -> throwable.message ?: "未知错误"
        }
        _error.tryEmit(errorMessage)
    }

    /**
     * 显示Toast消息
     */
    protected fun showToast(message: String) {
        _toastMessage.tryEmit(message)
    }

    /**
     * 处理Result结果
     */
    protected fun <T> handleResult(
        result: Result<T>,
        onSuccess: (T) -> Unit = {},
        onError: (String) -> Unit = { handleError(Exception(it)) },
        onLoading: () -> Unit = { setLoading(true) }
    ) {
        when (result) {
            is Result.Success -> {
                setLoading(false)
                onSuccess(result.data)
            }
            is Result.Error -> {
                setLoading(false)
                onError(result.exception.message ?: "未知错误")
            }
            is Result.Loading -> {
                onLoading()
            }
        }
    }
}

/**
 * 自定义异常类型
 */

/**
 * 网络异常
 */
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * 服务器异常
 */
class ServerException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * 业务逻辑异常
 */
class BusinessException(message: String, cause: Throwable? = null) : Exception(message, cause)
