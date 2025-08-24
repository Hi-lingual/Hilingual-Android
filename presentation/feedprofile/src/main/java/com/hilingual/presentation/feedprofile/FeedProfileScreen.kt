package com.hilingual.presentation.feedprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.component.BlockBottomSheet
import com.hilingual.presentation.feedprofile.component.FeedProfileInfo
import com.hilingual.presentation.feedprofile.component.FeedProfileTabRow
import com.hilingual.presentation.feedprofile.component.FeedUserActionButton
import com.hilingual.presentation.feedprofile.component.ReportBlockBottomSheet
import com.hilingual.presentation.feedprofile.model.LikeDiaryItemModel
import com.hilingual.presentation.feedprofile.model.SharedDiaryItemModel
import com.hilingual.presentation.feedprofile.tab.LikedDiaryScreen
import com.hilingual.presentation.feedprofile.tab.SharedDiaryScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FeedProfileScreen(
    onBackClick: () -> Unit,
    isMine: Boolean,
    profileUrl: String,
    nickname: String,
    follower: Int,
    following: Int,
    streak: Int,
    isFollowing: Boolean,
    isFollowed: Boolean,
    isBlock: Boolean,
    sharedDiarys: ImmutableList<SharedDiaryItemModel>,
    likedDiarys: ImmutableList<LikeDiaryItemModel>,
    onProfileClick: () -> Unit,
    onLikeClick: () -> Unit,
    onFollowTypeClick: () -> Unit,
    onFollowButtonClick: () -> Unit,
    onFeedContentClick: () -> Unit,
    onMenuClick: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var isMenuBottomSheetVisible by remember { mutableStateOf(false) }
    var isBlockBottomSheetVisible by remember { mutableStateOf(false) }

    val profileListState = rememberLazyListState()

    val isFabVisible by remember {
        derivedStateOf {
            profileListState.firstVisibleItemIndex > 0 || profileListState.firstVisibleItemScrollOffset > 0
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
    ) {
        if (isMine || isBlock) {
            BackTopAppBar(
                title = null,
                onBackClicked = onBackClick
            )
        } else {
            BackAndMoreTopAppBar(
                title = null,
                onBackClicked = onBackClick,
                onMoreClicked = { isMenuBottomSheetVisible = true }
            )
        }

        LazyColumn(
            state = profileListState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                FeedProfileInfo(
                    profileUrl = profileUrl,
                    nickname = nickname,
                    streak = streak,
                    follower = follower,
                    following = following,
                    onFollowTypeClick = onFollowTypeClick,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 20.dp)
                )
            }

            if (!isMine) {
                item {
                    FeedUserActionButton(
                        isFilled = isFollowing,
                        buttonText = when {
                            isBlock -> "차단 해제"
                            isFollowing -> "팔로잉"
                            isFollowed -> "맞팔로우"
                            else -> "팔로우"
                        },
                        onClick = onFollowButtonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            if (isBlock) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(140.dp))

                        Text(
                            text = "${nickname}님의 글을 확인할 수 없어요.",
                            style = HilingualTheme.typography.headB18,
                            color = HilingualTheme.colors.black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "차단을 해제하면 글을 확인할 수 있어요.",
                            style = HilingualTheme.typography.bodyM16,
                            color = HilingualTheme.colors.gray400,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                if (isMine) {
                    stickyHeader {
                        FeedProfileTabRow(
                            tabIndex = pagerState.currentPage,
                            onTabSelected = { index ->
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(HilingualTheme.colors.white)
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillParentMaxHeight()
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                when (page) {
                                    0 -> SharedDiaryScreen(
                                        sharedDiarys = sharedDiarys,
                                        onProfileClick = onProfileClick,
                                        onSharedDiaryClick = onFeedContentClick,
                                        onLikeClick = onLikeClick,
                                        onMenuClick = {
                                            onMenuClick()
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    1 -> LikedDiaryScreen(
                                        likedDiarys = likedDiarys,
                                        onProfileClick = onProfileClick,
                                        onLikeDiaryClick = onFeedContentClick,
                                        onLikeClick = onLikeClick,
                                        onMenuClick = {
                                            onMenuClick()
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            HilingualFloatingButton(
                                isVisible = isFabVisible,
                                onClick = {
                                    coroutineScope.launch {
                                        profileListState.animateScrollToItem(0)
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 24.dp, end = 16.dp)
                            )
                        }
                    }
                } else {
                    item {
                        SharedDiaryScreen(
                            sharedDiarys = sharedDiarys,
                            onProfileClick = onProfileClick,
                            onSharedDiaryClick = onFeedContentClick,
                            onLikeClick = onLikeClick,
                            onMenuClick = {
                                onMenuClick()
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .fillParentMaxHeight()
                        )
                    }
                }
            }
        }
    }
    ReportBlockBottomSheet(
        isVisible = isMenuBottomSheetVisible,
        onDismiss = { isMenuBottomSheetVisible = false },
        onReportClick = {
            isMenuBottomSheetVisible = false
            onReportClick()
        },
        onBlockClick = {
            isMenuBottomSheetVisible = false
            isBlockBottomSheetVisible = true
        }
    )
    BlockBottomSheet(
        isVisible = isBlockBottomSheetVisible,
        onDismiss = { isBlockBottomSheetVisible = false },
        onBlockButtonClick = {
            isBlockBottomSheetVisible = false
            onBlockClick()
        }
    )
}

private fun getSampleSharedDiaryData() = persistentListOf(
    SharedDiaryItemModel(
        profileImageUrl = "https://picsum.photos/id/64/100/100",
        diaryId = 1L,
        sharedDate = 30L,
        diaryImgUrl = "https://picsum.photos/id/1015/800/600",
        originalText = "I started learning a new language today! It felt difficult at first, but it’s becoming more fun little by little. I’ll try to practice every day, even just a bit.",
        nickname = "LanguageLover",
        likeCount = 24,
        isLiked = false
    ),
    SharedDiaryItemModel(
        profileImageUrl = "https://picsum.photos/id/213/100/100",
        diaryId = 2L,
        sharedDate = 120L,
        diaryImgUrl = "https://picsum.photos/id/201/800/600",
        originalText = "Weekend party with international friends! I had to speak only in English, and surprisingly it went well. I feel much more confident now.",
        nickname = "PartyLover",
        likeCount = 67,
        isLiked = true
    )
)

private fun getSampleLikedDiaryData() = persistentListOf(
    LikeDiaryItemModel(
        userId = 101L,
        isMine = false,
        profileImageUrl = "https://picsum.photos/id/319/100/100",
        nickname = "GrammarMaster",
        streak = 45,
        diaryId = 201L,
        sharedDate = 60L,
        likeCount = 89,
        isLiked = true,
        diaryImgUrl = "https://picsum.photos/id/311/800/600",
        originalText = "After studying grammar books for so long, I finally had a real conversation. It felt like stepping into a whole new world!"
    ),
    LikeDiaryItemModel(
        userId = 104L,
        isMine = false,
        profileImageUrl = "https://picsum.photos/id/399/100/100",
        nickname = "DramaFan",
        streak = 21,
        diaryId = 202L,
        sharedDate = 180L,
        likeCount = 73,
        isLiked = true,
        diaryImgUrl = null,
        originalText = "My goal was to watch a drama without subtitles, and I finally finished one episode! I didn’t understand everything, but the sense of achievement was amazing."
    )
)

@Preview(showBackground = true)
@Composable
private fun FeedProfileScreenPreview() {
    HilingualTheme {
        var isFollowingState by remember { mutableStateOf(false) }
        val isFollowedState by remember { mutableStateOf(false) }

        FeedProfileScreen(
            onBackClick = { },
            isMine = false,
            profileUrl = "https://picsum.photos/id/238/200/300",
            nickname = "OtherUser123",
            follower = 101,
            following = 52,
            streak = 11,
            isFollowing = isFollowingState,
            isFollowed = isFollowedState,
            isBlock = false,
            sharedDiarys = getSampleSharedDiaryData(),
            likedDiarys = getSampleLikedDiaryData(),
            onProfileClick = { },
            onLikeClick = { },
            onFollowTypeClick = { },
            onFollowButtonClick = {
                isFollowingState = !isFollowingState
            },
            onFeedContentClick = { },
            onMenuClick = { },
            onReportClick = { },
            onBlockClick = { }
        )
    }
}
