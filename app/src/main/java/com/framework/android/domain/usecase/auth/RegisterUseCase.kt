package com.framework.android.domain.usecase.auth

import com.framework.android.base.BaseUseCase
import com.framework.android.base.Result
import com.framework.android.domain.model.AuthToken
import com.framework.android.domain.model.User
import com.framework.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 注册用例
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<RegisterUseCase.Params, Pair<User, AuthToken>>() {
    
    override suspend fun execute(parameters: Params): Pair<User, AuthToken> {
        // 验证输入参数
        validateParams(parameters)
        
        // 执行注册
        val result = authRepository.register(parameters.username, parameters.email, parameters.password)
        
        return when (result) {
            is Result.Success -> {
                val (user, token) = result.data
                // 保存用户会话
                authRepository.saveUserSession(user, token)
                result.data
            }
            is Result.Error -> throw result.exception
            is Result.Loading -> throw IllegalStateException("Unexpected loading state")
        }
    }
    
    /**
     * 验证参数
     */
    private fun validateParams(params: Params) {
        when {
            params.username.isBlank() -> throw IllegalArgumentException("用户名不能为空")
            params.username.length < 3 -> throw IllegalArgumentException("用户名长度不能少于3位")
            params.email.isBlank() -> throw IllegalArgumentException("邮箱不能为空")
            !isValidEmail(params.email) -> throw IllegalArgumentException("邮箱格式不正确")
            params.password.isBlank() -> throw IllegalArgumentException("密码不能为空")
            params.password.length < 6 -> throw IllegalArgumentException("密码长度不能少于6位")
            params.password != params.confirmPassword -> throw IllegalArgumentException("两次输入的密码不一致")
        }
    }
    
    /**
     * 验证邮箱格式
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * 注册参数
     */
    data class Params(
        val username: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    )
}
