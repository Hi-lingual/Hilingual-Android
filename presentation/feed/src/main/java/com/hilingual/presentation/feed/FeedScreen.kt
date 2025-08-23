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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.tabrow.HilingualBasicTabRow
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feed.component.FeedTopAppBar
import com.hilingual.presentation.feed.model.FeedPreviewUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FeedRoute(
    paddingValues: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val toastTrigger = LocalToastTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FeedSideEffect.ShowToast -> {
                toastTrigger(it.message)
            }
        }
    }

    when (state) {
        is UiState.Loading -> {
            //TODO: 로딩 처리
        }

        is UiState.Success -> {
            FeedScreen(
                paddingValues = paddingValues,
                uiState = (state as UiState.Success<FeedUiState>).data,
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
    readAllFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    val recommendListState = rememberLazyListState()
    val followingsListState = rememberLazyListState()

    val isFabVisible by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> recommendListState.firstVisibleItemScrollOffset > 5
                else -> followingsListState.firstVisibleItemScrollOffset > 5
            }
        }
    }

    val isAtBottom by remember {
        derivedStateOf {
            val (currentListState, feedCount) = when (pagerState.currentPage) {
                0 -> recommendListState to uiState.recommendFeedList.size
                else -> followingsListState to uiState.followingFeedList.size
            }

            val layoutInfo = currentListState.layoutInfo
            val visibleItemInfo = layoutInfo.visibleItemsInfo

            if (feedCount == 0 || layoutInfo.totalItemsCount == 0) return@derivedStateOf false

            val lastVisibleItem = visibleItemInfo.last()
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) && (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
        }
    }

    val latestReadAllFeed by rememberUpdatedState(newValue = readAllFeed)

    LaunchedEffect(isAtBottom, pagerState.currentPage) {
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
            profileImageUrl = "",
            onProfileClick = {},
            onSearchClick = {}
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
                        feedList = uiState.recommendFeedList,
                        onProfileClick = {},
                        onMenuClick = {},
                        onContentClick = {},
                        onLikeClick = {},
                        onMoreClick = {}
                    )

                    1 -> FeedTabScreen(
                        listState = followingsListState,
                        feedList = uiState.followingFeedList,
                        onProfileClick = {},
                        onMenuClick = {},
                        onContentClick = {},
                        onLikeClick = {},
                        onMoreClick = {},
                        hasFollowing = uiState.hasFollowing
                    )
                }
            }

            HilingualFloatingButton(
                isVisible = isFabVisible,
                onClick = {
                    coroutineScope.launch {
                        when (pagerState.currentPage) {
                            0 -> recommendListState.animateScrollToItem(0)
                            else -> followingsListState.animateScrollToItem(0)
                        }
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
            readAllFeed = {},
            uiState = FeedUiState(
                recommendFeedList = persistentListOf(
                    FeedPreviewUiModel(
                        userId = 1,
                        profileUrl = "",
                        nickname = "작나",
                        streak = 15,
                        sharedDateInMinutes = 5L,
                        content = "Just enjoyed a beautiful sunset over the mountains! #travel #nature",
                        imageUrl = "",
                        diaryId = 1,
                        likeCount = 120,
                        isLiked = false
                    ),
                    FeedPreviewUiModel(
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
                    FeedPreviewUiModel(
                        userId = 3,
                        profileUrl = "",
                        nickname = "효비",
                        streak = 99,
                        sharedDateInMinutes = 60L * 24 * 3,
                        content = "Finished an amazing novel. Highly recommend it to anyone who loves a good mystery. " +
                                "The plot twists were incredible, and the characters were so well-developed. " +
                                "I couldn't put it down until I reached the very last page!",
                        imageUrl = "",
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