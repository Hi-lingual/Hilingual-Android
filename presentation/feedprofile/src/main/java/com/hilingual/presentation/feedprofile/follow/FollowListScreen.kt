package com.hilingual.presentation.feedprofile.follow

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.follow.component.FollowTabRow
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.follow.model.FollowTabType
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun FollowListRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    viewModel: FollowListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FollowListScreen(
        paddingValues = paddingValues,
        followers = uiState.followerList,
        followings = uiState.followingList,
        isFollowerRefreshing = uiState.isFollowerRefreshing,
        isFollowingRefreshing = uiState.isFollowingRefreshing,
        onBackClick = navigateUp,
        onProfileClick = navigateToFeedProfile,
        onActionButtonClick = viewModel::updateFollowingState,
        onTabRefresh = viewModel::refreshTab
    )
}

@Composable
private fun FollowListScreen(
    paddingValues: PaddingValues,
    followers: UiState<ImmutableList<FollowItemModel>>,
    followings: UiState<ImmutableList<FollowItemModel>>,
    isFollowerRefreshing: Boolean,
    isFollowingRefreshing: Boolean,
    onBackClick: () -> Unit,
    onProfileClick: (Long) -> Unit,
    onActionButtonClick: (Long, Boolean, FollowTabType) -> Unit,
    onTabRefresh: (FollowTabType) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val followerListState = rememberLazyListState()
    val followingListState = rememberLazyListState()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
            val tabType = if (it == 0) FollowTabType.FOLLOWER else FollowTabType.FOLLOWING
            onTabRefresh(tabType)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackTopAppBar(
            title = "팔로우",
            onBackClicked = onBackClick
        )
        FollowTabRow(
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
            val (followState, emptyCardType, tabType, isRefreshing, listState) = when (page) {
                0 -> {
                    val state = Triple(followers, FeedEmptyCardType.NO_FOLLOWER, FollowTabType.FOLLOWER)
                    Tuple5(state.first, state.second, state.third, isFollowerRefreshing, followerListState)
                }
                else -> {
                    val state = Triple(followings, FeedEmptyCardType.NO_FOLLOWING, FollowTabType.FOLLOWING)
                    Tuple5(state.first, state.second, state.third, isFollowingRefreshing, followingListState)
                }
            }

            when (followState) {
                is UiState.Loading -> HilingualLoadingIndicator()
                is UiState.Success -> {
                    FollowScreen(
                        follows = followState.data,
                        emptyCardType = emptyCardType,
                        isRefreshing = isRefreshing,
                        listState = listState,
                        onRefresh = { onTabRefresh(tabType) },
                        onProfileClick = onProfileClick,
                        onActionButtonClick = { userId, isFollowing ->
                            onActionButtonClick(userId, isFollowing, tabType)
                        }
                    )
                }
                else -> {}
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

@Preview(showBackground = true)
@Composable
private fun FollowListScreenPreview() {
    HilingualTheme {
        FollowListScreen(
            paddingValues = PaddingValues(0.dp),
            followers = FollowListUiState.Fake.followerList,
            followings = FollowListUiState.Fake.followingList,
            isFollowerRefreshing = false,
            isFollowingRefreshing = false,
            onBackClick = {},
            onProfileClick = {},
            onActionButtonClick = { _, _, _ -> },
            onTabRefresh = {}
        )
    }
}
