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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.subScreenPadding
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.util.RetryOnReconnect
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.view.HilingualLoadErrorView
import com.hilingual.core.designsystem.component.view.LoadErrorViewAction
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
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { NotificationTab.entries.size })
    val currentTab = NotificationTab.entries[pagerState.currentPage]
    val currentTabState = uiState.tabState(currentTab)
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is NotificationSideEffect.ShowErrorDialog -> {
                dialogTrigger.show(onClick = { viewModel.refreshTab(sideEffect.tab) })
            }
        }
    }

    LaunchedEffect(currentTab) {
        viewModel.loadTab(currentTab)
    }

    RetryOnReconnect(
        isLoading = currentTabState.loadState is UiState.Loading,
        shouldRetry = currentTabState.loadState is UiState.Failure,
        onRetry = { viewModel.refreshTab(currentTab) },
    )

    if (currentTabState.loadState is UiState.Failure) {
        HilingualLoadErrorView(
            action = LoadErrorViewAction.Retry(
                onRetryClick = { viewModel.refreshTab(currentTab) },
                onBackClick = navigateUp,
            ),
            modifier = Modifier.padding(paddingValues),
        )
        return
    }

    NotificationScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        pagerState = pagerState,
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
        onTabRefresh = viewModel::refreshTab,
    )
}

@Composable
private fun NotificationScreen(
    uiState: NotificationUiState,
    paddingValues: PaddingValues,
    pagerState: PagerState,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    onFeedNotificationClick: (FeedNotificationItemUiModel) -> Unit,
    onNoticeNotificationClick: (NoticeNotificationItemUiModel) -> Unit,
    onTabRefresh: (NotificationTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val feedListState = rememberLazyListState()
    val noticeListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState, feedListState, noticeListState) {
        snapshotFlow { pagerState.currentPage }
            .withIndex()
            .collect { (index, page) ->
                val tab = NotificationTab.entries[page]
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
            .subScreenPadding(paddingValues),
    ) {
        NotificationTopAppBar(
            onBackClick = onBackClick,
            onSettingClick = {
                coroutineScope.launch {
                    onSettingClick()
                    pagerState.scrollToPage(0)
                }
            },
        )
        NotificationTapRow(
            tabIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (val tab = NotificationTab.entries[page]) {
                NotificationTab.FEED -> FeedScreen(
                    notifications = uiState.feedNotifications,
                    onNotificationClick = onFeedNotificationClick,
                    isRefreshing = uiState.tabState(tab).isRefreshing,
                    listState = feedListState,
                    onRefresh = { onTabRefresh(tab) },
                )

                NotificationTab.NOTIFICATION -> NoticeScreen(
                    notifications = uiState.noticeNotifications,
                    onNotificationClick = onNoticeNotificationClick,
                    isRefreshing = uiState.tabState(tab).isRefreshing,
                    listState = noticeListState,
                    onRefresh = { onTabRefresh(tab) },
                )
            }
        }
    }
}
