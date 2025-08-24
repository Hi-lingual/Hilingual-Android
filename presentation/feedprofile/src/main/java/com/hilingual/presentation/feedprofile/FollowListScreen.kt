package com.hilingual.presentation.feedprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.component.FollowTabRow
import com.hilingual.presentation.feedprofile.model.FollowItemModel
import com.hilingual.presentation.feedprofile.tab.FollowScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FollowListScreen(
    followings: ImmutableList<FollowItemModel>,
    followers: ImmutableList<FollowItemModel>,
    onBackClick: () -> Unit,
    onProfileClick: (Long) -> Unit,
    onButtonClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
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
            when (page) {
                0 -> {
                    FollowScreen(
                        follow = followers,
                        onProfileClick = onProfileClick,
                        onButtonClick = onButtonClick
                    )
                }

                1 -> FollowScreen(
                    follow = followings,
                    onProfileClick = onProfileClick,
                    onButtonClick = onButtonClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowScreenPreview() {
    HilingualTheme {
        FollowListScreen(
            followings = persistentListOf(
                FollowItemModel(
                    userId = 1L,
                    profileImgUrl = "",
                    nickname = "큰나",
                    isFollowing = true,
                    isFollowed = true
                ),
                FollowItemModel(
                    userId = 2L,
                    profileImgUrl = "",
                    nickname = "작나",
                    isFollowing = false,
                    isFollowed = true
                ),
                FollowItemModel(
                    userId = 3L,
                    profileImgUrl = "",
                    nickname = "지영",
                    isFollowing = false,
                    isFollowed = false
                )
            ),
            followers = persistentListOf(
                FollowItemModel(
                    userId = 4L,
                    profileImgUrl = "",
                    nickname = "효빈",
                    isFollowing = true,
                    isFollowed = true
                ),
                FollowItemModel(
                    userId = 5L,
                    profileImgUrl = "",
                    nickname = "민재",
                    isFollowing = false,
                    isFollowed = false
                )
            ),
            onBackClick = {},
            onProfileClick = {},
            onButtonClick = {}
        )
    }
}
