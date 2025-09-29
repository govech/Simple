package com.framework.android.data.remote.api

import com.framework.android.data.remote.dto.BaseResponse
import com.framework.android.data.remote.dto.PageResponse
import com.framework.android.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 用户相关的API服务
 */
interface UserService {

    /**
     * 获取用户信息
     */
    @GET("users/{userId}")
    suspend fun getUserInfo(
        @Path("userId") userId: Long
    ): BaseResponse<UserDto>

    /**
     * 获取当前用户信息
     */
    @GET("users/me")
    suspend fun getCurrentUser(): BaseResponse<UserDto>

    /**
     * 获取用户列表（分页）
     */
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("keyword") keyword: String? = null
    ): BaseResponse<PageResponse<UserDto>>

    /**
     * 搜索用户
     */
    @GET("users/search")
    suspend fun searchUsers(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): BaseResponse<PageResponse<UserDto>>
}
