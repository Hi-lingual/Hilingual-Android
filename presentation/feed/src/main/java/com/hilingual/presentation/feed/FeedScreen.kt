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
package com.hilingual.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.tabrow.HilingualBasicTabRow
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feed.component.FeedTopAppBar
import com.hilingual.presentation.feed.model.FeedListItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FeedRoute(
    paddingValues: PaddingValues,
    navigateToMyFeedProfile: () -> Unit,
    navigateToFeedProfile: (userId: Long) -> Unit,
    navigateToFeedDiary: (Long) -> Unit,
    navigateToFeedSearch: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toastTrigger = LocalToastTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FeedSideEffect.ShowToast -> {
                toastTrigger(it.message)
            }
        }
    }

    with(uiState) {
        FeedScreen(
            paddingValues = paddingValues,
            myProfileUrl = myProfileUrl,
            recommendFeedList = recommendFeedList,
            followingFeedList = followingFeedList,
            onSearchClick = navigateToFeedSearch,
            onMyProfileClick = navigateToMyFeedProfile,
            onFeedProfileClick = navigateToFeedProfile,
            onLikeClick = viewModel::toggleIsLiked,
            onContentDetailClick = navigateToFeedDiary,
            onUnpublishClick = viewModel::diaryUnpublish,
            onReportClick = { context.launchCustomTabs(UrlConstant.FEEDBACK_REPORT) },
            readAllFeed = viewModel::readAllFeed
        )
    }
}

@Composable
private fun FeedScreen(
    paddingValues: PaddingValues,
    myProfileUrl: String,
    recommendFeedList: UiState<ImmutableList<FeedListItemUiModel>>,
    followingFeedList: UiState<ImmutableList<FeedListItemUiModel>>,
    onMyProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFeedProfileClick: (Long) -> Unit,
    onLikeClick: (Long, Boolean) -> Unit,
    onContentDetailClick: (Long) -> Unit,
    onUnpublishClick: (Long) -> Unit,
    onReportClick: () -> Unit,
    readAllFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    val recommendListState = rememberLazyListState()
    val followingsListState = rememberLazyListState()

    val currentListState = remember(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> recommendListState
            else -> followingsListState
        }
    }

    val isFabVisible by remember(pagerState.currentPage) {
        derivedStateOf {
            currentListState.firstVisibleItemScrollOffset > 5
        }
    }

    val isAtBottom by remember(pagerState.currentPage) {
        derivedStateOf {
            val feedListState = when (pagerState.currentPage) {
                0 -> recommendFeedList
                else -> followingFeedList
            }

            when (feedListState) {
                is UiState.Success -> {
                    val feedList = feedListState.data
                    val layoutInfo = currentListState.layoutInfo
                    val visibleItemsInfo = layoutInfo.visibleItemsInfo

                    if (feedList.isEmpty() || layoutInfo.totalItemsCount == 0) return@derivedStateOf false

                    val lastVisibleItem = visibleItemsInfo.last()
                    val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                    (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) &&
                        (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
                }
                else -> false
            }
        }
    }

    val latestReadAllFeed by rememberUpdatedState(newValue = readAllFeed)

    LaunchedEffect(isAtBottom, pagerState.currentPage) {
        // TODO: 리스트 끌어당김 이벤트 감지
        if (isAtBottom) {
            latestReadAllFeed()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        FeedTopAppBar(
            profileImageUrl = myProfileUrl,
            onProfileClick = onMyProfileClick,
            onSearchClick = onSearchClick
        )

        HilingualBasicTabRow(
            tabTitles = persistentListOf("추천", "팔로잉"),
            tabIndex = pagerState.currentPage,
            onTabSelected = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> FeedTabScreen(
                        listState = recommendListState,
                        feedListState = recommendFeedList,
                        onProfileClick = onFeedProfileClick,
                        onContentDetailClick = onContentDetailClick,
                        onLikeClick = onLikeClick,
                        hasFollowing = false,
                        onUnpublishClick = onUnpublishClick,
                        onReportClick = onReportClick
                    )
                    1 -> FeedTabScreen(
                        listState = followingsListState,
                        feedListState = followingFeedList,
                        onProfileClick = onFeedProfileClick,
                        onContentDetailClick = onContentDetailClick,
                        onLikeClick = onLikeClick,
                        hasFollowing = false,
                        onUnpublishClick = onUnpublishClick,
                        onReportClick = onReportClick
                    )
                }
            }

            HilingualFloatingButton(
                isVisible = isFabVisible,
                onClick = {
                    coroutineScope.launch {
                        currentListState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    HilingualTheme {
        FeedScreen(
            paddingValues = PaddingValues(),
            onMyProfileClick = {},
            onSearchClick = {},
            onFeedProfileClick = {},
            onContentDetailClick = {},
            onLikeClick = { _, _ -> },
            readAllFeed = {},
            onUnpublishClick = {},
            onReportClick = {},
            myProfileUrl = "",
            recommendFeedList = UiState.Success(persistentListOf()),
            followingFeedList = UiState.Success(persistentListOf())
        )
    }
}
