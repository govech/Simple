package com.framework.android.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * 权限管理助手类
 * 简化权限请求和检查的操作
 */
class PermissionHelper(
    private val activity: ComponentActivity
) {
    
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var onPermissionResult: ((Map<String, Boolean>) -> Unit)? = null
    
    init {
        setupPermissionLauncher()
    }
    
    private fun setupPermissionLauncher() {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            onPermissionResult?.invoke(permissions)
        }
    }
    
    /**
     * 检查单个权限是否已授权
     */
    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, 
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 检查多个权限是否都已授权
     */
    fun hasPermissions(vararg permissions: String): Boolean {
        return permissions.all { hasPermission(it) }
    }
    
    /**
     * 请求单个权限
     */
    fun requestPermission(
        permission: String,
        onResult: (Boolean) -> Unit
    ) {
        requestPermissions(arrayOf(permission)) { results ->
            onResult(results[permission] == true)
        }
    }
    
    /**
     * 请求多个权限
     */
    fun requestPermissions(
        permissions: Array<String>,
        onResult: (Map<String, Boolean>) -> Unit
    ) {
        onPermissionResult = onResult
        permissionLauncher?.launch(permissions)
    }
    
    /**
     * 检查权限并在需要时请求
     */
    fun checkAndRequestPermission(
        permission: String,
        onGranted: () -> Unit,
        onDenied: (() -> Unit)? = null,
        onShowRationale: (() -> Unit)? = null
    ) {
        when {
            hasPermission(permission) -> {
                onGranted()
            }
            activity.shouldShowRequestPermissionRationale(permission) -> {
                onShowRationale?.invoke() ?: run {
                    requestPermission(permission) { granted ->
                        if (granted) onGranted() else onDenied?.invoke()
                    }
                }
            }
            else -> {
                requestPermission(permission) { granted ->
                    if (granted) onGranted() else onDenied?.invoke()
                }
            }
        }
    }
    
    /**
     * 跳转到应用设置页面
     */
    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }
    
    companion object {
        /**
         * 常用权限常量
         */
        object Permissions {
            const val CAMERA = Manifest.permission.CAMERA
            const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
            const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
            const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
            const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
            const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
            const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
            const val CALL_PHONE = Manifest.permission.CALL_PHONE
            const val SEND_SMS = Manifest.permission.SEND_SMS
            const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
            
            // 权限组
            val LOCATION = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            val STORAGE = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
        }
        
        /**
         * 检查权限是否已授权（静态方法）
         */
        fun hasPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context, 
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        
        /**
         * 检查多个权限是否都已授权（静态方法）
         */
        fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            return permissions.all { hasPermission(context, it) }
        }
    }
}

/**
 * Fragment扩展函数
 */
fun Fragment.createPermissionHelper(): PermissionHelper? {
    return (activity as? ComponentActivity)?.let { 
        PermissionHelper(it) 
    }
}

/**
 * Activity扩展函数
 */
fun ComponentActivity.createPermissionHelper(): PermissionHelper {
    return PermissionHelper(this)
}
