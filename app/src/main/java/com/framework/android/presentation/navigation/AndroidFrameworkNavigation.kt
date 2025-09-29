package com.framework.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.framework.android.presentation.ui.screens.HomeScreen
import com.framework.android.presentation.ui.screens.LoginScreen
import com.framework.android.presentation.ui.screens.SplashScreen
import com.framework.android.presentation.viewmodel.MainViewModel

/**
 * 应用程序导航组件
 * 管理应用程序的页面导航
 */
@Composable
fun AndroidFrameworkNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()
    val isFirstLaunch by mainViewModel.isFirstLaunch.collectAsState()
    
    // 根据登录状态和首次启动状态确定起始页面
    val startDestination = when {
        isFirstLaunch -> Screen.Splash.route
        isLoggedIn -> Screen.Home.route
        else -> Screen.Login.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 启动屏
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // 登录屏
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        // 注册屏
        composable(Screen.Register.route) {
            // TODO: 实现注册屏幕
        }
        
        // 主屏
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        // 设置屏
        composable(Screen.Settings.route) {
            // TODO: 实现设置屏幕
        }
        
        // 用户详情屏
        composable(
            route = Screen.UserDetail.route,
            arguments = Screen.UserDetail.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toLongOrNull()
            if (userId != null) {
                // TODO: 实现用户详情屏幕
            }
        }
    }
}
