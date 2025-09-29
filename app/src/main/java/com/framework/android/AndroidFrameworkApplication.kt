package com.framework.android

import android.app.Application
import com.framework.android.utils.Logger
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用程序入口类
 * 使用Hilt进行依赖注入
 */
@HiltAndroidApp
class AndroidFrameworkApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        Logger.i("Application", "AndroidFrameworkApplication started")
        
        // 初始化应用程序级别的配置
        initializeApp()
    }
    
    /**
     * 初始化应用程序
     */
    private fun initializeApp() {
        // 这里可以添加应用程序启动时需要的初始化逻辑
        // 例如：崩溃报告、性能监控、推送服务等
        
        Logger.d("Application", "App initialization completed")
    }
    
    override fun onTerminate() {
        super.onTerminate()
        Logger.i("Application", "AndroidFrameworkApplication terminated")
    }
    
    override fun onLowMemory() {
        super.onLowMemory()
        Logger.w("Application", "Low memory warning")
    }
    
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Logger.w("Application", "Trim memory level: $level")
    }
}
