package com.hilingual.presentation.feedprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
    ) {
        if (isMine) {
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
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .fillParentMaxHeight()

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
        originalText = "오늘은 새로운 언어를 배우기 시작했어요! 처음엔 어려웠지만 점점 재미있어지고 있어요. 매일 조금씩이라도 꾸준히 해보려고 합니다.",
        nickname = "언어러버",
        likeCount = 24,
        isLiked = false
    ),
    SharedDiaryItemModel(
        profileImageUrl = "https://picsum.photos/id/91/100/100",
        diaryId = 2L,
        sharedDate = 120L,
        diaryImgUrl = null,
        originalText = "카페에서 공부하다가 만난 새로운 친구와 대화를 나눴어요. 서로 다른 문화에 대해 이야기하면서 많은 것을 배웠습니다.",
        nickname = "카페탐험가",
        likeCount = 18,
        isLiked = true
    ),
    SharedDiaryItemModel(
        profileImageUrl = "https://picsum.photos/id/177/100/100",
        diaryId = 3L,
        sharedDate = 360L,
        diaryImgUrl = "https://picsum.photos/id/119/800/600",
        originalText = "드디어 첫 번째 언어교환 세션을 완료했어요! 떨렸지만 정말 유익한 시간이었습니다. 다음 세션이 벌써 기대돼요.",
        nickname = "초보학습자",
        likeCount = 42,
        isLiked = false
    ),
    SharedDiaryItemModel(
        profileImageUrl = "https://picsum.photos/id/213/100/100",
        diaryId = 4L,
        sharedDate = 480L,
        diaryImgUrl = "https://picsum.photos/id/201/800/600",
        originalText = "주말에 외국인 친구들과 함께한 파티! 영어로만 대화해야 하는 상황이었는데 생각보다 잘 통했어요. 자신감이 많이 생겼습니다.",
        nickname = "파티러버",
        likeCount = 67,
        isLiked = true
    ),
    SharedDiaryItemModel(
        profileImageUrl = "https://picsum.photos/id/245/100/100",
        diaryId = 5L,
        sharedDate = 720L,
        diaryImgUrl = null,
        originalText = "매일 일기 쓰기 도전 7일째! 처음엔 한 줄도 쓰기 어려웠는데 이제는 생각이 술술 나와요. 꾸준함의 힘을 느끼고 있어요.",
        nickname = "꾸준이",
        likeCount = 35,
        isLiked = false
    )
)

private fun getSampleLikedDiaryData() = persistentListOf(
    LikeDiaryItemModel(
        userId = 101L,
        isMine = false,
        profileImageUrl = "https://picsum.photos/id/319/100/100",
        nickname = "문법마스터",
        streak = 45,
        diaryId = 201L,
        sharedDate = 60L,
        likeCount = 89,
        isLiked = true,
        diaryImgUrl = "https://picsum.photos/id/311/800/600",
        originalText = "문법책만 보다가 실제 대화를 해보니 완전히 다른 세계더라고요. 책으로만 배우는 것의 한계를 깨달았어요."
    ),
    LikeDiaryItemModel(
        userId = 102L,
        isMine = false,
        profileImageUrl = "https://picsum.photos/id/342/100/100",
        nickname = "길잡이",
        streak = 9,
        diaryId = 202L,
        sharedDate = 180L,
        likeCount = 56,
        isLiked = true,
        diaryImgUrl = null,
        originalText = "오늘 처음으로 외국인에게 길을 알려줬어요! 설명이 잘 통했을 때의 그 뿌듯함이란... 정말 짜릿했습니다."
    ),
    LikeDiaryItemModel(
        userId = 103L,
        isMine = false,
        profileImageUrl = "https://picsum.photos/id/375/100/100",
        nickname = "앱러버",
        streak = 12,
        diaryId = 203L,
        sharedDate = 300L,
        likeCount = 31,
        isLiked = true,
        diaryImgUrl = "https://picsum.photos/id/367/800/600",
        originalText = "언어 학습 앱으로 새로운 단어들을 배우고 있어요. 게임처럼 재미있게 할 수 있어서 중독성이 있네요!"
    ),
    LikeDiaryItemModel(
        userId = 104L,
        isMine = false,
        profileImageUrl = "https://picsum.photos/id/399/100/100",
        nickname = "드라마매니아",
        streak = 21,
        diaryId = 204L,
        sharedDate = 540L,
        likeCount = 73,
        isLiked = true,
        diaryImgUrl = null,
        originalText = "드라마를 자막 없이 보는 게 목표였는데, 드디어 한 편을 완주했어요! 모든 걸 이해하지는 못했지만 성취감이 엄청나요."
    )
)

@Preview(showBackground = true, name = "FeedProfileScreen - My Profile")
@Composable
private fun FeedProfileScreenPreviewMyProfile() {
    HilingualTheme {
        FeedProfileScreen(
            onBackClick = { println("Preview: Back clicked") },
            isMine = true,
            profileUrl = "https://picsum.photos/id/237/200/300",
            nickname = "MyOwnProfile",
            follower = 155,
            following = 70,
            streak = 22,
            isFollowing = false,
            isFollowed = false,
            isBlock = false,
            sharedDiarys = getSampleSharedDiaryData(),
            likedDiarys = getSampleLikedDiaryData(),
            onProfileClick = { },
            onLikeClick = { },
            onFollowTypeClick = { },
            onFollowButtonClick = { /* 내 프로필에서는 호출 안 됨 */ },
            onFeedContentClick = { },
            onMenuClick = { },
            onReportClick = { },
            onBlockClick = { }
        )
    }
}

@Preview(showBackground = true, name = "FeedProfileScreen - Other's Profile (Not Following)")
@Composable
private fun FeedProfileScreenPreviewOthersNotFollowing() {
    HilingualTheme {
        var isFollowingState by remember { mutableStateOf(false) }
        val isFollowedState by remember { mutableStateOf(false) }

        FeedProfileScreen(
            onBackClick = { println("Preview: Back clicked") },
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

@Preview(showBackground = true, name = "FeedProfileScreen - Other's Profile (Following)")
@Composable
private fun FeedProfileScreenPreviewOthersFollowing() {
    HilingualTheme {
        var isFollowingState by remember { mutableStateOf(true) }
        val isFollowedState by remember { mutableStateOf(false) }

        FeedProfileScreen(
            onBackClick = { },
            isMine = false,
            profileUrl = "https://picsum.photos/id/239/200/300",
            nickname = "AlreadyFollowing",
            follower = 205,
            following = 125,
            streak = 35,
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

@Preview(showBackground = true, name = "FeedProfileScreen - Other's Profile (They Follow Me)")
@Composable
private fun FeedProfileScreenPreviewOthersTheyFollow() {
    HilingualTheme {
        var isFollowingState by remember { mutableStateOf(false) }
        val isFollowedState by remember { mutableStateOf(true) }

        FeedProfileScreen(
            onBackClick = { println("Preview: Back clicked") },
            isMine = false,
            profileUrl = "https://picsum.photos/id/240/200/300",
            nickname = "ShouldFollowBack",
            follower = 185,
            following = 95,
            streak = 7,
            isFollowing = isFollowingState,
            isFollowed = isFollowedState,
            isBlock = true,
            sharedDiarys = getSampleSharedDiaryData(),
            likedDiarys = getSampleLikedDiaryData(),
            onProfileClick = { },
            onLikeClick = { },
            onFollowTypeClick = { },
            onFollowButtonClick = {
                isFollowingState = true
            },
            onFeedContentClick = { },
            onMenuClick = { },
            onReportClick = { },
            onBlockClick = { }
        )
    }
}
