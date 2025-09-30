package com.framework.android.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.framework.android.presentation.ui.theme.AndroidFrameworkTheme
import com.framework.android.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

/**
 * 深度链接处理Activity
 * 用于处理从外部链接打开应用的逻辑
 */
@AndroidEntryPoint
class DeepLinkActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Logger.i("DeepLinkActivity", "DeepLinkActivity created")
        
        // 处理深度链接
        handleDeepLink(intent)
        
        enableEdgeToEdge()
        
        setContent {
            DeepLinkContent()
        }
    }
    
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleDeepLink(it)
        }
    }
    
    /**
     * 处理深度链接
     */
    private fun handleDeepLink(intent: Intent) {
        val action = intent.action
        val data: Uri? = intent.data
        
        if (Intent.ACTION_VIEW == action && data != null) {
            val path = data.path
            val parameters = data.queryParameterNames.associateWith { 
                data.getQueryParameter(it) 
            }
            
            Logger.d("DeepLinkActivity", "Received deep link: $data")
            Logger.d("DeepLinkActivity", "Path: $path")
            Logger.d("DeepLinkActivity", "Parameters: $parameters")
            
            // 这里可以根据不同的路径处理不同的逻辑
            when (path) {
                "/login" -> {
                    // 处理登录页面的深度链接
                    Logger.d("DeepLinkActivity", "Handling login deep link")
                }
                "/home" -> {
                    // 处理主页的深度链接
                    Logger.d("DeepLinkActivity", "Handling home deep link")
                }
                else -> {
                    // 处理其他路径
                    Logger.d("DeepLinkActivity", "Handling unknown path: $path")
                }
            }
        }
    }
}

@Composable
private fun DeepLinkContent() {
    AndroidFrameworkTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Text(
                text = "处理深度链接中...",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        }
    }
}