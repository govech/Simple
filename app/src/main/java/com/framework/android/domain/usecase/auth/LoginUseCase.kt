package com.framework.android.domain.usecase.auth

import com.framework.android.base.BaseUseCase
import com.framework.android.base.Result
import com.framework.android.domain.model.AuthToken
import com.framework.android.domain.model.User
import com.framework.android.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 登录用例
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginUseCase.Params, Pair<User, AuthToken>>() {
    
    override suspend fun execute(parameters: Params): Pair<User, AuthToken> {
        // 验证输入参数
        validateParams(parameters)
        
        // 执行登录
        val result = authRepository.login(parameters.username, parameters.password)
        
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
            params.password.isBlank() -> throw IllegalArgumentException("密码不能为空")
            params.password.length < 6 -> throw IllegalArgumentException("密码长度不能少于6位")
        }
    }
    
    /**
     * 登录参数
     */
    data class Params(
        val username: String,
        val password: String
    )
}
