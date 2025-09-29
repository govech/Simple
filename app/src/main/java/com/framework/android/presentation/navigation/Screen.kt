package com.framework.android.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * 应用程序屏幕定义
 * 定义所有的页面路由和参数
 */
sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    /**
     * 启动屏
     */
    data object Splash : Screen("splash")
    
    /**
     * 登录屏
     */
    data object Login : Screen("login")
    
    /**
     * 注册屏
     */
    data object Register : Screen("register")
    
    /**
     * 主屏
     */
    data object Home : Screen("home")
    
    /**
     * 设置屏
     */
    data object Settings : Screen("settings")
    
    /**
     * 用户详情屏
     */
    data object UserDetail : Screen(
        route = "user_detail/{userId}",
        arguments = listOf(
            navArgument("userId") {
                type = NavType.StringType
            }
        )
    ) {
        /**
         * 创建带参数的路由
         */
        fun createRoute(userId: Long): String {
            return "user_detail/$userId"
        }
    }
    
    /**
     * 用户列表屏
     */
    data object UserList : Screen("user_list")
    
    /**
     * 搜索屏
     */
    data object Search : Screen("search")
    
    /**
     * 个人资料屏
     */
    data object Profile : Screen("profile")
    
    /**
     * 关于屏
     */
    data object About : Screen("about")
}

/**
 * 底部导航栏项目
 */
enum class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: String // 这里可以使用图标资源ID或者图标名称
) {
    HOME(Screen.Home, "首页", "home"),
    SEARCH(Screen.Search, "搜索", "search"),
    PROFILE(Screen.Profile, "我的", "person")
}
