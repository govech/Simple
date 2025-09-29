package com.framework.android.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.framework.android.data.local.dao.UserDao
import com.framework.android.data.local.entity.UserEntity

/**
 * 应用数据库
 * 使用Room数据库框架
 */
@Database(
    entities = [
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    // DAO接口
    abstract fun userDao(): UserDao
    
    companion object {
        const val DATABASE_NAME = "app_database"
        
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // 在开发阶段可以使用，生产环境需要提供迁移策略
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
