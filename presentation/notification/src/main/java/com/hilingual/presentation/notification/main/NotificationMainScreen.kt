package com.hilingual.presentation.notification.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.notification.main.component.NotificationTapRow
import com.hilingual.presentation.notification.main.component.NotificationTopAppBar
import com.hilingual.presentation.notification.main.model.FeedNotificationItemModel
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemModel
import com.hilingual.presentation.notification.main.tab.FeedScreen
import com.hilingual.presentation.notification.main.tab.NoticeScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationMainScreen(
    feedNotifications: ImmutableList<FeedNotificationItemModel>,
    noticeNotifications: ImmutableList<NoticeNotificationItemModel>,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    onFeedNotificationClick: (String) -> Unit,
    onNoticeNotificationClick: (Long) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        NotificationTopAppBar(
            onBackClick = onBackClick,
            onSettingClick = onSettingClick
        )
        NotificationTapRow(
            tabIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> FeedScreen(
                    notifications = feedNotifications,
                    onNotificationClick = onFeedNotificationClick
                )

                1 -> NoticeScreen(
                    notifications = noticeNotifications,
                    onNotificationClick = onNoticeNotificationClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationMainScreenPreview() {
    val feedNotifications = persistentListOf<FeedNotificationItemModel>()
    val noticeNotifications = persistentListOf(
        NoticeNotificationItemModel(
            id = 0,
            title = "v 1.1.0 업데이트 알림",
            date = "2025.08.05",
            isRead = true
        )
    )

    HilingualTheme {
        NotificationMainScreen(
            feedNotifications = feedNotifications,
            noticeNotifications = noticeNotifications,
            onBackClick = {},
            onSettingClick = {},
            onFeedNotificationClick = {},
            onNoticeNotificationClick = {},
            paddingValues = PaddingValues(0.dp)
        )
    }
}
