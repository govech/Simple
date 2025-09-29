package com.framework.android.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * 扩展函数集合
 * 提供常用的扩展功能
 */

/**
 * Context扩展函数
 */

/**
 * 显示Toast消息
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * 显示长Toast消息
 */
fun Context.showLongToast(message: String) {
    showToast(message, Toast.LENGTH_LONG)
}

/**
 * String扩展函数
 */

/**
 * 检查字符串是否为有效邮箱
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * 检查字符串是否为有效手机号
 */
fun String.isValidPhone(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}

/**
 * 将字符串转换为首字母大写
 */
fun String.capitalizeFirst(): String {
    return if (isEmpty()) this else this[0].uppercaseChar() + substring(1)
}

/**
 * 安全地转换字符串为Int
 */
fun String.toIntOrDefault(default: Int = 0): Int {
    return toIntOrNull() ?: default
}

/**
 * 安全地转换字符串为Long
 */
fun String.toLongOrDefault(default: Long = 0L): Long {
    return toLongOrNull() ?: default
}

/**
 * 安全地转换字符串为Float
 */
fun String.toFloatOrDefault(default: Float = 0f): Float {
    return toFloatOrNull() ?: default
}

/**
 * 安全地转换字符串为Double
 */
fun String.toDoubleOrDefault(default: Double = 0.0): Double {
    return toDoubleOrNull() ?: default
}

/**
 * Collection扩展函数
 */

/**
 * 安全地获取列表元素
 */
fun <T> List<T>.safeGet(index: Int): T? {
    return if (index in indices) this[index] else null
}

/**
 * 检查列表是否不为空
 */
fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return this != null && isNotEmpty()
}

/**
 * Compose扩展函数
 */

/**
 * 收集Flow并执行副作用
 */
@Composable
fun <T> Flow<T>.collectAsEffect(
    block: suspend (T) -> Unit
) {
    LaunchedEffect(this) {
        collectLatest(block)
    }
}

/**
 * 显示Toast的Compose扩展
 */
@Composable
fun Flow<String>.collectAsToast() {
    val context = LocalContext.current
    collectAsEffect { message ->
        if (message.isNotBlank()) {
            context.showToast(message)
        }
    }
}

/**
 * Number扩展函数
 */

/**
 * 将字节数转换为可读的文件大小
 */
fun Long.toReadableFileSize(): String {
    if (this < 1024) return "$this B"
    
    val units = arrayOf("KB", "MB", "GB", "TB")
    var size = this.toDouble()
    var unitIndex = -1
    
    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }
    
    return "%.1f %s".format(size, units[unitIndex])
}

/**
 * 将毫秒转换为可读的时间格式
 */
fun Long.toReadableDuration(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        days > 0 -> "${days}天"
        hours > 0 -> "${hours}小时"
        minutes > 0 -> "${minutes}分钟"
        else -> "${seconds}秒"
    }
}

/**
 * Boolean扩展函数
 */

/**
 * Boolean转换为可见性字符串
 */
fun Boolean.toVisibilityString(): String {
    return if (this) "显示" else "隐藏"
}

/**
 * Boolean转换为启用状态字符串
 */
fun Boolean.toEnabledString(): String {
    return if (this) "启用" else "禁用"
}
