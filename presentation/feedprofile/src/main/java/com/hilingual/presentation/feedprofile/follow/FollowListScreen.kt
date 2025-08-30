package com.hilingual.presentation.feedprofile.follow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
        onActionButtonClick = { _, _ ->
        }
    )
}

@Composable
private fun FollowListScreen(
    paddingValues: PaddingValues,
    followers: UiState<ImmutableList<FollowItemModel>>,
    followings: UiState<ImmutableList<FollowItemModel>>,
    onBackClick: () -> Unit,
    onProfileClick: (Long) -> Unit,
    onActionButtonClick: (Long, Boolean) -> Unit,
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
            val (followState, emptyCardType) = when (page) {
                0 -> followers to FeedEmptyCardType.NO_FOLLOWER
                else -> followings to FeedEmptyCardType.NO_FOLLOWING
            }

            when (followState) {
                is UiState.Loading -> HilingualLoadingIndicator()
                is UiState.Success -> {
                    FollowScreen(
                        follows = followState.data,
                        emptyCardType = emptyCardType,
                        onProfileClick = onProfileClick,
                        onActionButtonClick = onActionButtonClick
                    )
                }

                else -> {}
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
            onActionButtonClick = { _, _ -> }
        )
    }
}
