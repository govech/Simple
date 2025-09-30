package com.framework.android.data.mapper

import com.framework.android.data.local.entity.UserEntity
import com.framework.android.data.remote.dto.UserDto
import com.framework.android.domain.model.User
import com.framework.android.utils.DateTimeUtils
import java.util.Date

/**
 * 用户数据映射器
 * 负责在不同层级的用户数据模型之间进行转换
 */
object UserMapper {
    
    /**
     * DTO转领域模型
     */
    fun UserDto.toDomainModel(): User {
        return User(
            id = id,
            username = username,
            email = email,
            avatar = avatar,
            createdAt = DateTimeUtils.parseDate(createdAt, DateTimeUtils.FORMAT_ISO_8601) ?: Date(),
            updatedAt = DateTimeUtils.parseDate(updatedAt, DateTimeUtils.FORMAT_ISO_8601) ?: Date()
        )
    }
    
    /**
     * 领域模型转Entity
     */
    fun User.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            email = email,
            avatar = avatar,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastSyncTime = Date()
        )
    }
    
    /**
     * Entity转领域模型
     */
    fun UserEntity.toDomainModel(): User {
        return User(
            id = id,
            username = username,
            email = email,
            avatar = avatar,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    /**
     * DTO转Entity
     */
    fun UserDto.toEntity(): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            email = email,
            avatar = avatar,
            createdAt = DateTimeUtils.parseDate(createdAt, DateTimeUtils.FORMAT_ISO_8601) ?: Date(),
            updatedAt = DateTimeUtils.parseDate(updatedAt, DateTimeUtils.FORMAT_ISO_8601) ?: Date(),
            lastSyncTime = Date()
        )
    }
    
    /**
     * DTO列表转领域模型列表
     */
    fun List<UserDto>.toDomainModelList(): List<User> {
        return map { it.toDomainModel() }
    }
    
    /**
     * 领域模型列表转Entity列表
     */
    fun List<User>.toEntityList(): List<UserEntity> {
        return map { it.toEntity() }
    }
    
    /**
     * Entity列表转领域模型列表
     */
    fun List<UserEntity>.entityToDomainModelList(): List<User> {
        return map { it.toDomainModel() }
    }
    
    /**
     * DTO列表转Entity列表
     */
    fun List<UserDto>.dtoToEntityList(): List<UserEntity> {
        return map { it.toEntity() }
    }
}
