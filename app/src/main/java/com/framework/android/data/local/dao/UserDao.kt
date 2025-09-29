package com.framework.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.framework.android.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 用户数据访问对象
 * 定义用户表的数据库操作
 */
@Dao
interface UserDao {
    
    /**
     * 获取所有用户
     */
    @Query("SELECT * FROM users ORDER BY username ASC")
    fun getAllUsers(): Flow<List<UserEntity>>
    
    /**
     * 根据ID获取用户
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?
    
    /**
     * 根据ID获取用户（Flow版本）
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: Long): Flow<UserEntity?>
    
    /**
     * 根据用户名获取用户
     */
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
    
    /**
     * 根据邮箱获取用户
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    /**
     * 搜索用户
     */
    @Query("SELECT * FROM users WHERE username LIKE :keyword OR email LIKE :keyword ORDER BY username ASC")
    fun searchUsers(keyword: String): Flow<List<UserEntity>>
    
    /**
     * 分页获取用户
     */
    @Query("SELECT * FROM users ORDER BY username ASC LIMIT :limit OFFSET :offset")
    suspend fun getUsersPaged(limit: Int, offset: Int): List<UserEntity>
    
    /**
     * 获取用户总数
     */
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
    
    /**
     * 插入用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long
    
    /**
     * 插入多个用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>): List<Long>
    
    /**
     * 更新用户
     */
    @Update
    suspend fun updateUser(user: UserEntity): Int
    
    /**
     * 删除用户
     */
    @Delete
    suspend fun deleteUser(user: UserEntity): Int
    
    /**
     * 根据ID删除用户
     */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Long): Int
    
    /**
     * 删除所有用户
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers(): Int
    
    /**
     * 检查用户是否存在
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE id = :userId)")
    suspend fun userExists(userId: Long): Boolean
    
    /**
     * 更新用户最后同步时间
     */
    @Query("UPDATE users SET last_sync_time = :syncTime WHERE id = :userId")
    suspend fun updateLastSyncTime(userId: Long, syncTime: java.util.Date): Int
}
