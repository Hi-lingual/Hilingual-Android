package com.hilingual.core.designsystem.component.pulltorefresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hilingual.core.designsystem.theme.HilingualTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HilingualPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable (BoxScope.() -> Unit)
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        contentAlignment = contentAlignment,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = state,
                containerColor = HilingualTheme.colors.white,
                color = HilingualTheme.colors.black
            )
        },
        content = content
    )
}
