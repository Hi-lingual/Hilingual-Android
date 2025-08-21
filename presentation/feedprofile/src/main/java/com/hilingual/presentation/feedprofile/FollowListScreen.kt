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
import com.hilingual.presentation.feedprofile.model.FollowerItemModel
import com.hilingual.presentation.feedprofile.model.FollowingItemModel
import com.hilingual.presentation.feedprofile.tab.FollowerScreen
import com.hilingual.presentation.feedprofile.tab.FollowingScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FollowScreen(
    followings: ImmutableList<FollowingItemModel>,
    followers: ImmutableList<FollowerItemModel>,
    onBackClick: () -> Unit,
    onFollowingItemClick: (Long) -> Unit,
    onFollowerItemClick: (Long) -> Unit,
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
                0 -> FollowerScreen(
                    followers = followers,
                    onFollowerItemClick = onFollowerItemClick,
                    onButtonClick = onButtonClick
                )

                1 -> FollowingScreen(
                    followings = followings,
                    onFollowingItemClick = onFollowingItemClick,
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
        FollowScreen(
            followings = persistentListOf(
                FollowingItemModel(
                    userId = 1L,
                    profileImgUrl = "",
                    nickname = "큰나",
                    isFollowing = true,
                    isFollowed = true
                ),
                FollowingItemModel(
                    userId = 2L,
                    profileImgUrl = "",
                    nickname = "작나",
                    isFollowing = false,
                    isFollowed = true
                ),
                FollowingItemModel(
                    userId = 3L,
                    profileImgUrl = "",
                    nickname = "지영",
                    isFollowing = false,
                    isFollowed = false
                )
            ),
            followers = persistentListOf(
                FollowerItemModel(
                    userId = 4L,
                    profileImgUrl = "",
                    nickname = "효빈",
                    isFollowing = true,
                    isFollowed = true
                ),
                FollowerItemModel(
                    userId = 5L,
                    profileImgUrl = "",
                    nickname = "민재",
                    isFollowing = false,
                    isFollowed = false
                )
            ),
            onBackClick = {},
            onFollowingItemClick = {},
            onFollowerItemClick = {},
            onButtonClick = {}
        )
    }
}
