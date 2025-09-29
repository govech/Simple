package com.framework.android.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.framework.android.presentation.navigation.AndroidFrameworkNavigation
import com.framework.android.presentation.ui.theme.AndroidFrameworkTheme
import com.framework.android.presentation.viewmodel.MainViewModel
import com.framework.android.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主Activity
 * 应用程序的入口Activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Logger.i("MainActivity", "MainActivity created")
        
        enableEdgeToEdge()
        
        setContent {
            MainContent()
        }
    }
    
    override fun onStart() {
        super.onStart()
        Logger.d("MainActivity", "MainActivity started")
    }
    
    override fun onResume() {
        super.onResume()
        Logger.d("MainActivity", "MainActivity resumed")
    }
    
    override fun onPause() {
        super.onPause()
        Logger.d("MainActivity", "MainActivity paused")
    }
    
    override fun onStop() {
        super.onStop()
        Logger.d("MainActivity", "MainActivity stopped")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Logger.i("MainActivity", "MainActivity destroyed")
    }
}

@Composable
private fun MainContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val navController = rememberNavController()
    
    AndroidFrameworkTheme(
        darkTheme = when (themeMode) {
            "dark" -> true
            "light" -> false
            else -> androidx.compose.foundation.isSystemInDarkTheme()
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AndroidFrameworkNavigation(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
