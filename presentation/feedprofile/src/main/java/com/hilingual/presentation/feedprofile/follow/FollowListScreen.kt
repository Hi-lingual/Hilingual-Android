package com.hilingual.presentation.feedprofile.follow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    onBackClick: () -> Unit,
    onProfileClick: (Long) -> Unit,
    onActionButtonClick: (Long, Boolean, FollowTabType) -> Unit,
    onTabRefresh: (FollowTabType) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var previousPage by remember { mutableStateOf(pagerState.currentPage) }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != previousPage) {
            val tabType = if (pagerState.currentPage == 0) FollowTabType.FOLLOWER else FollowTabType.FOLLOWING
            onTabRefresh(tabType)
            previousPage = pagerState.currentPage
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
                    val tabType = if (index == 0) FollowTabType.FOLLOWER else FollowTabType.FOLLOWING
                    onTabRefresh(tabType)
                }
            }
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val (followState, emptyCardType, tabType) = when (page) {
                0 -> Triple(followers, FeedEmptyCardType.NO_FOLLOWER, FollowTabType.FOLLOWER)
                else -> Triple(followings, FeedEmptyCardType.NO_FOLLOWING, FollowTabType.FOLLOWING)
            }

            key(pagerState.currentPage == page) {
                when (followState) {
                    is UiState.Loading -> HilingualLoadingIndicator()
                    is UiState.Success -> {
                        FollowScreen(
                            follows = followState.data,
                            emptyCardType = emptyCardType,
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
}

@Preview(showBackground = true)
@Composable
private fun FollowListScreenPreview() {
    HilingualTheme {
        FollowListScreen(
            paddingValues = PaddingValues(0.dp),
            followers = FollowListUiState.Fake.followerList,
            followings = FollowListUiState.Fake.followingList,
            onBackClick = {},
            onProfileClick = {},
            onActionButtonClick = { _, _, _ -> },
            onTabRefresh = {}
        )
    }
}
