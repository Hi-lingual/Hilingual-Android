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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.tabrow.HilingualBasicTabRow
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feed.component.FeedTopAppBar
import com.hilingual.presentation.feed.model.FeedListItemUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FeedRoute(
    paddingValues: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toastTrigger = LocalToastTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FeedSideEffect.ShowToast -> {
                toastTrigger(it.message)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> HilingualLoadingIndicator()

        is UiState.Success -> {
            FeedScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onProfileClick = {
                    // TODO: 피드 프로필 화면으로 이동
                },
                onSearchClick = {
                    // TODO: 친구 검색 화면으로 이동
                },
                readAllFeed = viewModel::readAllFeed
            )
        }
        else -> {}
    }
}

@Composable
private fun FeedScreen(
    paddingValues: PaddingValues,
    uiState: FeedUiState,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    readAllFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    val recommendListState = rememberLazyListState()
    val followingsListState = rememberLazyListState()

    val (currentListState, feedList) = remember(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> recommendListState to uiState.recommendFeedList
            else -> followingsListState to uiState.followingFeedList
        }
    }

    val isFabVisible by remember(pagerState.currentPage) {
        derivedStateOf {
            currentListState.firstVisibleItemScrollOffset > 5
        }
    }

    val isAtBottom by remember(pagerState.currentPage) {
        derivedStateOf {
            val layoutInfo = currentListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (feedList.isEmpty() || layoutInfo.totalItemsCount == 0) return@derivedStateOf false

            val lastVisibleItem = visibleItemsInfo.last()
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) && (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
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
            profileImageUrl = uiState.myProfileUrl,
            onProfileClick = onProfileClick,
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
                FeedTabScreen(
                    listState = currentListState,
                    feedList = feedList,
                    onProfileClick = {},
                    onMenuClick = {},
                    onContentClick = {},
                    onLikeClick = {},
                    onMoreClick = {},
                    hasFollowing = if (page == 1) uiState.hasFollowing else false
                )
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
            onProfileClick = {},
            onSearchClick = {},
            readAllFeed = {},
            uiState = FeedUiState(
                myProfileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
                recommendFeedList = persistentListOf(
                    FeedListItemUiModel(
                        userId = 1,
                        profileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
                        nickname = "작나",
                        streak = 15,
                        sharedDateInMinutes = 5L,
                        content = "Just enjoyed a beautiful sunset over the mountains! #travel #nature",
                        imageUrl = "https://velog.velcdn.com/images/nahy-512/post/49cfbf3d-504f-4c2a-b0b9-458f287735e6/image.png",
                        diaryId = 1,
                        likeCount = 120,
                        isLiked = false
                    ),
                    FeedListItemUiModel(
                        userId = 2,
                        profileUrl = "",
                        nickname = "한민돌",
                        streak = 3,
                        sharedDateInMinutes = 60L * 2,
                        content = "Trying out a new recipe tonight. It's a bit spicy but delicious! 🌶️",
                        imageUrl = null,
                        diaryId = 2,
                        likeCount = 75,
                        isLiked = true
                    ),
                    FeedListItemUiModel(
                        userId = 3,
                        profileUrl = "",
                        nickname = "효비",
                        streak = 99,
                        sharedDateInMinutes = 60L * 24 * 3,
                        content = "Finished an amazing novel. Highly recommend it to anyone who loves a good mystery. " +
                            "The plot twists were incredible, and the characters were so well-developed. " +
                            "I couldn't put it down until I reached the very last page!",
                        imageUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=774&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        diaryId = 3,
                        likeCount = 210,
                        isLiked = false
                    )
                ),
                followingFeedList = persistentListOf(),
                hasFollowing = false
            )
        )
    }
}
