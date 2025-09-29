package com.framework.android.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.framework.android.R

/**
 * 错误状态组件
 * 当发生错误时显示的通用组件
 */
@Composable
fun ErrorState(
    title: String = "出错了",
    message: String = stringResource(id = R.string.error_unknown),
    icon: Painter = rememberVectorPainter(Icons.Default.ErrorOutline),
    actionText: String = stringResource(id = R.string.retry),
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 错误图标
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 标题
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 错误消息
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        // 重试按钮
        if (onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(onClick = onActionClick) {
                Text(text = actionText)
            }
        }
    }
}

/**
 * 网络错误状态组件
 */
@Composable
fun NetworkErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorState(
        title = "网络连接失败",
        message = stringResource(id = R.string.error_network),
        onActionClick = onRetry,
        modifier = modifier
    )
}

/**
 * 服务器错误状态组件
 */
@Composable
fun ServerErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorState(
        title = "服务器错误",
        message = stringResource(id = R.string.error_server),
        onActionClick = onRetry,
        modifier = modifier
    )
}
