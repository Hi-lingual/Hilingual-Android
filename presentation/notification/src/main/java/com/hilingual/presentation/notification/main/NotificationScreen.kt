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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.notification.main.component.NotificationTapRow
import com.hilingual.presentation.notification.main.component.NotificationTopAppBar
import com.hilingual.presentation.notification.main.model.FeedNotificationItemUiModel
import com.hilingual.presentation.notification.main.model.FeedNotificationType
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemUiModel
import com.hilingual.presentation.notification.main.tab.FeedScreen
import com.hilingual.presentation.notification.main.tab.NoticeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.withIndex
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
        paddingValues = paddingValues,
        onBackClick = navigateUp,
        onSettingClick = navigateToSetting,
        onFeedNotificationClick = { notification ->
            viewModel.readNotification(notification.id)
            when (notification.feedType) {
                FeedNotificationType.LIKE_DIARY -> navigateToFeedDiary(notification.targetId)
                FeedNotificationType.FOLLOW_USER -> navigateToFeedProfile(notification.targetId)
            }
        },
        onNoticeNotificationClick = { notification -> navigateToNoticeDetail(notification.id) },
        onTabSelected = viewModel::onTabSelected,
        onUserRefresh = viewModel::onUserRefresh
    )
}

@Composable
private fun NotificationScreen(
    uiState: NotificationUiState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    onFeedNotificationClick: (FeedNotificationItemUiModel) -> Unit,
    onNoticeNotificationClick: (NoticeNotificationItemUiModel) -> Unit,
    onTabSelected: (NotificationTab) -> Unit,
    onUserRefresh: (NotificationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val feedListState = rememberLazyListState()
    val noticeListState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState, feedListState, noticeListState) {
        snapshotFlow { pagerState.currentPage }
            .withIndex()
            .collect { (index, page) ->
                val tab = NotificationTab.entries[page]
                onTabSelected(tab)
                if (index > 0) {
                    delay(100)
                    when (tab) {
                        NotificationTab.FEED -> feedListState.animateScrollToItem(0)
                        NotificationTab.NOTIFICATION -> noticeListState.animateScrollToItem(0)
                    }
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
            onSettingClick = {
                coroutineScope.launch {
                    onSettingClick()
                    pagerState.scrollToPage(0)
                }
            }
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
            when (val tab = NotificationTab.entries[page]) {
                NotificationTab.FEED -> FeedScreen(
                    notifications = uiState.feedNotifications,
                    onNotificationClick = onFeedNotificationClick,
                    isRefreshing = uiState.isFeedRefreshing,
                    listState = feedListState,
                    onRefresh = { onUserRefresh(tab) }
                )

                NotificationTab.NOTIFICATION -> NoticeScreen(
                    notifications = uiState.noticeNotifications,
                    onNotificationClick = onNoticeNotificationClick,
                    isRefreshing = uiState.isNoticeRefreshing,
                    listState = noticeListState,
                    onRefresh = { onUserRefresh(tab) }
                )
            }
        }
    }
}