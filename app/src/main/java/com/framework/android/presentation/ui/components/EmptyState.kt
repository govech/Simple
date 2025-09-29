package com.framework.android.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.framework.android.R

/**
 * 空状态组件
 * 当没有数据时显示的通用组件
 */
@Composable
fun EmptyState(
    title: String = stringResource(id = R.string.empty_data_title),
    message: String = stringResource(id = R.string.empty_data_message),
    icon: Painter? = null,
    actionText: String? = null,
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
        // 图标
        icon?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // 标题
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 消息
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        // 操作按钮
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(onClick = onActionClick) {
                Text(text = actionText)
            }
        }
    }
}

/**
 * 搜索空状态组件
 */
@Composable
fun SearchEmptyState(
    keyword: String,
    onRetrySearch: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = stringResource(id = R.string.empty_search_title),
        message = stringResource(id = R.string.empty_search_message),
        actionText = if (onRetrySearch != null) "重新搜索" else null,
        onActionClick = onRetrySearch,
        modifier = modifier
    )
}
