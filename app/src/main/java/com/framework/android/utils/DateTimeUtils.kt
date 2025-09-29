package com.framework.android.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 日期时间工具类
 * 提供常用的日期时间处理功能
 */
object DateTimeUtils {
    
    // 常用日期格式
    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_MM_DD_HH_MM = "MM-dd HH:mm"
    const val FORMAT_HH_MM = "HH:mm"
    const val FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    
    private val defaultLocale = Locale.getDefault()
    
    /**
     * 格式化日期
     */
    fun formatDate(date: Date?, pattern: String = FORMAT_YYYY_MM_DD_HH_MM): String {
        if (date == null) return ""
        return try {
            SimpleDateFormat(pattern, defaultLocale).format(date)
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * 格式化时间戳
     */
    fun formatTimestamp(timestamp: Long, pattern: String = FORMAT_YYYY_MM_DD_HH_MM): String {
        return formatDate(Date(timestamp), pattern)
    }
    
    /**
     * 解析日期字符串
     */
    fun parseDate(dateString: String, pattern: String = FORMAT_YYYY_MM_DD_HH_MM_SS): Date? {
        return try {
            SimpleDateFormat(pattern, defaultLocale).parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 获取相对时间描述（如：刚刚、5分钟前、昨天等）
     */
    fun getRelativeTimeString(date: Date?): String {
        if (date == null) return ""
        
        val now = System.currentTimeMillis()
        val time = date.time
        val diff = now - time
        
        return when {
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> {
                val hours = diff / (60 * 60 * 1000)
                if (hours < 1) "刚刚" else "${hours}小时前"
            }
            diff < 2 * 24 * 60 * 60 * 1000 -> "昨天"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}天前"
            else -> formatDate(date, FORMAT_YYYY_MM_DD)
        }
    }
    
    /**
     * 获取今天的开始时间（00:00:00）
     */
    fun getTodayStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    /**
     * 获取今天的结束时间（23:59:59）
     */
    fun getTodayEnd(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    /**
     * 获取本周开始时间（周一00:00:00）
     */
    fun getWeekStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    /**
     * 获取本月开始时间
     */
    fun getMonthStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    /**
     * 判断两个日期是否为同一天
     */
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
    
    /**
     * 判断日期是否为今天
     */
    fun isToday(date: Date?): Boolean {
        return isSameDay(date, Date())
    }
    
    /**
     * 判断日期是否为昨天
     */
    fun isYesterday(date: Date?): Boolean {
        if (date == null) return false
        
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.time
        
        return isSameDay(date, yesterday)
    }
    
    /**
     * 计算两个日期之间的天数差
     */
    fun getDaysBetween(startDate: Date, endDate: Date): Long {
        val diffInMillis = endDate.time - startDate.time
        return diffInMillis / (24 * 60 * 60 * 1000)
    }
    
    /**
     * 添加天数
     */
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }
    
    /**
     * 添加小时
     */
    fun addHours(date: Date, hours: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return calendar.time
    }
    
    /**
     * 添加分钟
     */
    fun addMinutes(date: Date, minutes: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }
    
    /**
     * 转换为UTC时间
     */
    fun toUTC(date: Date): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = date
        return calendar.time
    }
    
    /**
     * 从UTC时间转换为本地时间
     */
    fun fromUTC(date: Date): Date {
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCalendar.time = date
        
        val localCalendar = Calendar.getInstance()
        localCalendar.timeInMillis = utcCalendar.timeInMillis
        
        return localCalendar.time
    }
}
