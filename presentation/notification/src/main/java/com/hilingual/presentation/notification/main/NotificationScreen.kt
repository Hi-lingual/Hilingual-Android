/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.notification.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.notification.main.component.NotificationTapRow
import com.hilingual.presentation.notification.main.component.NotificationTopAppBar
import com.hilingual.presentation.notification.main.model.FeedNotificationItemModel
import com.hilingual.presentation.notification.main.model.FeedNotificationType
import com.hilingual.presentation.notification.main.tab.FeedScreen
import com.hilingual.presentation.notification.main.tab.NoticeScreen
import kotlinx.coroutines.launch

@Composable
internal fun NotificationRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToFeedDiary: (Long) -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToNoticeDetail: (Long) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onSettingClick = navigateToSetting,
        onFeedNotificationClick = { notification ->
            viewModel.readFeedNotification(notification.noticeId)
            when (notification.type) {
                FeedNotificationType.LIKE_DIARY -> navigateToFeedDiary(notification.targetId)
                FeedNotificationType.FOLLOW_USER -> navigateToFeedProfile(notification.targetId)
            }
        },
        onNoticeNotificationClick = navigateToNoticeDetail,
        onFeedRefresh = viewModel::refreshFeed,
        onNoticeRefresh = viewModel::refreshNotice,
        onFeedLoad = viewModel::loadFeed,
        onNoticeLoad = viewModel::loadNotice,
        paddingValues = paddingValues
    )
}

@Composable
private fun NotificationScreen(
    uiState: NotificationUiState,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    onFeedNotificationClick: (FeedNotificationItemModel) -> Unit,
    onNoticeNotificationClick: (Long) -> Unit,
    onFeedRefresh: () -> Unit,
    onNoticeRefresh: () -> Unit,
    onFeedLoad: () -> Unit,
    onNoticeLoad: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val feedListState = rememberLazyListState()
    val noticeListState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> {
                onFeedLoad()
                feedListState.animateScrollToItem(0)
            }
            1 -> {
                onNoticeLoad()
                noticeListState.animateScrollToItem(0)
            }
        }
    }

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
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FeedScreen(
                    notifications = uiState.feedNotifications,
                    onNotificationClick = onFeedNotificationClick,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onFeedRefresh,
                    listState = feedListState
                )

                1 -> NoticeScreen(
                    notifications = uiState.noticeNotifications,
                    onNotificationClick = onNoticeNotificationClick,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onNoticeRefresh,
                    listState = noticeListState
                )
            }
        }
    }
}
