package com.hilingual.presentation.feedprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.component.BlockBottomSheet
import com.hilingual.presentation.feedprofile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.component.FeedProfileInfo
import com.hilingual.presentation.feedprofile.component.FeedProfileTabRow
import com.hilingual.presentation.feedprofile.component.ReportBlockBottomSheet
import com.hilingual.presentation.feedprofile.model.FeedProfileInfoModel
import com.hilingual.presentation.feedprofile.model.LikeDiaryItemModel
import com.hilingual.presentation.feedprofile.model.SharedDiaryItemModel
import com.hilingual.presentation.feedprofile.tab.DiaryListScreen
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun FeedProfileRoute(
    paddingValues: PaddingValues,
    viewModel: FeedProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            HilingualLoadingIndicator()
        }

        is UiState.Success -> {
            FeedProfileScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onBackClick = { },
                onFollowTypeClick = { },
                onActionButtonClick = { _, _ -> },
                onProfileClick = { },
                onContentClick = { },
                onLikeClick = { },
                onMoreClick = { },
                onMenuClick = { },
                onReportClick = { },
                onBlockClick = { }
            )
        }

        else -> {}
    }
}

@Composable
private fun FeedProfileScreen(
    paddingValues: PaddingValues,
    uiState: FeedProfileUiState,
    onBackClick: () -> Unit,
    onFollowTypeClick: () -> Unit,
    onActionButtonClick: (Long, Boolean) -> Unit,
    onProfileClick: () -> Unit,
    onContentClick: () -> Unit,
    onLikeClick: () -> Unit,
    onMoreClick: () -> Unit,
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

    val profile = (uiState.feedProfileInfo as UiState.Success).data

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (profile.isMine || profile.isBlock) {
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

                    with(profile) {
                        FeedProfileInfo(
                            profileUrl = profileUrl,
                            nickname = nickname,
                            streak = streak,
                            follower = follower,
                            following = following,
                            onFollowTypeClick = onFollowTypeClick,
                            isMine = isMine,
                            isFollowing = isFollowing,
                            isFollowed = isFollowed,
                            isBlock = isBlock,
                            onActionButtonClick = { onActionButtonClick },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                when {
                    profile.isMine -> {
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
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                val (diaries, emptyCardType) = when (page) {
                                    0 -> uiState.sharedDiarys to FeedEmptyCardType.NOT_SHARED
                                    else -> uiState.likedDiarys to FeedEmptyCardType.NOT_LIKED
                                }

                                DiaryListScreen(
                                    diaries = diaries,
                                    emptyCardType = emptyCardType,
                                    onProfileClick = { onProfileClick },
                                    onContentClick = { onContentClick },
                                    onLikeClick = { onLikeClick },
                                    onMoreClick = { onMoreClick },
                                    onMenuClick = { onMenuClick },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    profile.isBlock -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(140.dp))

                                Text(
                                    text = "${profile.nickname}님의 글을 확인할 수 없어요.",
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
                    }

                    else -> {
                        item {
                            DiaryListScreen(
                                diaries = uiState.sharedDiarys,
                                emptyCardType = FeedEmptyCardType.NOT_SHARED,
                                onProfileClick = { onProfileClick },
                                onContentClick = { onContentClick },
                                onLikeClick = { onLikeClick },
                                onMenuClick = { onMenuClick },
                                onMoreClick = { onMoreClick },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .fillParentMaxHeight()
                            )
                        }
                    }
                }
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

@Preview(showBackground = true)
@Composable
private fun FeedProfileScreenPreview() {
    HilingualTheme {
        FeedProfileScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = FeedProfileUiState(
                feedProfileInfo = UiState.Success(
                    FeedProfileInfoModel(
                        profileUrl = "",
                        nickname = "하이링",
                        streak = 5,
                        follower = 120,
                        following = 98,
                        isMine = false,
                        isFollowing = true,
                        isFollowed = true,
                        isBlock = false
                    )
                ),
                sharedDiarys = persistentListOf(
                    SharedDiaryItemModel(
                        profileImageUrl = "",
                        nickname = "하이링",
                        diaryId = 1L,
                        sharedDate = 1720000000L,
                        likeCount = 12,
                        isLiked = true,
                        diaryImgUrl = null,
                        originalText = "오늘은 새로운 언어를 배웠다!"
                    )
                ),
                likedDiarys = persistentListOf(
                    LikeDiaryItemModel(
                        userId = 1L,
                        streak = 7,
                        profileImageUrl = "",
                        nickname = "링구",
                        diaryId = 2L,
                        sharedDate = 1720000500L,
                        likeCount = 30,
                        isLiked = false,
                        diaryImgUrl = null,
                        originalText = "이건 내가 좋아요한 일기!"
                    )
                )
            ),
            onBackClick = {},
            onActionButtonClick = { _, _ -> },
            onReportClick = {},
            onBlockClick = {},
            onFollowTypeClick = {},
            onProfileClick = {},
            onContentClick = {},
            onLikeClick = {},
            onMoreClick = {},
            onMenuClick = {}
        )
    }
}
