package com.hilingual.presentation.feedprofile.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.dialog.report.ReportUserDialog
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.component.topappbar.BackTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feedprofile.profile.component.BlockBottomSheet
import com.hilingual.presentation.feedprofile.profile.component.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.profile.component.FeedProfileInfo
import com.hilingual.presentation.feedprofile.profile.component.FeedProfileTabRow
import com.hilingual.presentation.feedprofile.profile.component.ReportBlockBottomSheet
import com.hilingual.presentation.feedprofile.profile.model.DiaryTabType
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
internal fun FeedProfileRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToFollowList: () -> Unit,
    navigateToFeedDiary: (Long) -> Unit,
    viewModel: FeedProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val toastTrigger = LocalToastTrigger.current
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FeedProfileSideEffect.ShowToast -> {
                toastTrigger(it.message)
            }
            is FeedProfileSideEffect.ShowRetryDialog -> dialogTrigger.show(it.onRetry)
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            HilingualLoadingIndicator()
        }

        is UiState.Success -> {
            FeedProfileScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onBackClick = navigateUp,
                onFollowClick = { isMine ->
                    if (isMine) {
                        navigateToFollowList()
                    }
                },
                onActionButtonClick = { isCurrentlyFollowing ->
                    if (isCurrentlyFollowing != null) {
                        viewModel.updateFollowingState(isCurrentlyFollowing)
                    }
                },
                onProfileClick = navigateToFeedProfile,
                onContentDetailClick = navigateToFeedDiary,
                onReportUserClick = { context.launchCustomTabs(UrlConstant.FEEDBACK_REPORT) },
                onLikeClick = viewModel::toggleIsLiked,
                onBlockClick = { viewModel.updateBlockState(state.data.feedProfileInfo.isBlock ?: false) },
                onReportDiaryClick = { context.launchCustomTabs(UrlConstant.FEEDBACK_REPORT) },
                onUnpublishClick = viewModel::diaryUnpublish,
                onTabRefresh = viewModel::refreshTab
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
    onFollowClick: (Boolean) -> Unit,
    onActionButtonClick: (Boolean?) -> Unit,
    onProfileClick: (Long) -> Unit,
    onContentDetailClick: (Long) -> Unit,
    onLikeClick: (Long, Boolean, DiaryTabType) -> Unit,
    onReportUserClick: () -> Unit,
    onBlockClick: () -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    onReportDiaryClick: () -> Unit,
    onTabRefresh: (DiaryTabType) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var isMenuBottomSheetVisible by remember { mutableStateOf(false) }
    var isBlockBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportUserDialogVisible by remember { mutableStateOf(false) }

    val profileListState = rememberLazyListState()
    var shouldEnableScroll by remember { mutableStateOf(true) }

    val canParentScrollForOthers by remember {
        derivedStateOf { profileListState.firstVisibleItemIndex >= 1 }
    }

    val canParentScrollForMine by remember {
        derivedStateOf { profileListState.firstVisibleItemIndex >= 2 }
    }

    val profile = uiState.feedProfileInfo
    val finalScrollEnabled = if (profile.isBlock == true) false else shouldEnableScroll

    val isFabVisible by remember {
        derivedStateOf {
            finalScrollEnabled && (
                profileListState.firstVisibleItemIndex > 0 ||
                    profileListState.firstVisibleItemScrollOffset > 0
                )
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
            val tabType = if (it == 0) DiaryTabType.SHARED else DiaryTabType.LIKED
            onTabRefresh(tabType)
            profileListState.animateScrollToItem(0)
        }
    }

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
            if (profile.isMine || profile.isBlock == true) {
                BackTopAppBar(
                    title = "피드",
                    onBackClicked = onBackClick
                )
            } else {
                BackAndMoreTopAppBar(
                    title = "피드",
                    onBackClicked = onBackClick,
                    onMoreClicked = { isMenuBottomSheetVisible = true }
                )
            }

            LazyColumn(
                state = profileListState,
                userScrollEnabled = finalScrollEnabled,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    with(profile) {
                        FeedProfileInfo(
                            profileImageUrl = profileImageUrl,
                            nickname = nickname,
                            streak = streak,
                            follower = follower,
                            following = following,
                            onFollowClick = { onFollowClick(isMine) },
                            isMine = isMine,
                            isFollowing = isFollowing,
                            isFollowed = isFollowed,
                            isBlock = isBlock,
                            onActionButtonClick = {
                                if (isBlock == true) {
                                    onBlockClick()
                                } else {
                                    onActionButtonClick(isFollowing)
                                }
                            },
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
                                modifier = Modifier.fillParentMaxSize()
                            ) { page ->
                                val (diaries, emptyCardType, tabType) = when (page) {
                                    0 -> Triple(uiState.sharedDiaries, FeedEmptyCardType.NOT_SHARED, DiaryTabType.SHARED)
                                    else -> Triple(uiState.likedDiaries, FeedEmptyCardType.NOT_LIKED, DiaryTabType.LIKED)
                                }

                                DiaryListScreen(
                                    diaries = diaries,
                                    emptyCardType = emptyCardType,
                                    onProfileClick = onProfileClick,
                                    onContentDetailClick = onContentDetailClick,
                                    onLikeClick = { diaryId, isLiked -> onLikeClick(diaryId, isLiked, tabType) },
                                    onUnpublishClick = onUnpublishClick,
                                    onReportClick = onReportDiaryClick,
                                    onScrollStateChanged = { isScrollable ->
                                        shouldEnableScroll = isScrollable
                                    },
                                    isNestedScroll = true,
                                    canParentScroll = canParentScrollForMine,
                                    modifier = Modifier.fillParentMaxSize()
                                )
                            }
                        }
                    }

                    profile.isBlock == true -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
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
                                diaries = uiState.sharedDiaries,
                                emptyCardType = FeedEmptyCardType.NOT_SHARED,
                                onProfileClick = onProfileClick,
                                onContentDetailClick = onContentDetailClick,
                                onLikeClick = { diaryId, isLiked -> onLikeClick(diaryId, isLiked, DiaryTabType.SHARED) },
                                onUnpublishClick = onUnpublishClick,
                                onReportClick = onReportDiaryClick,
                                onScrollStateChanged = { isScrollable ->
                                    shouldEnableScroll = isScrollable
                                },
                                isNestedScroll = true,
                                canParentScroll = canParentScrollForOthers,
                                modifier = Modifier.fillParentMaxSize()
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
                .navigationBarsPadding()
                .padding(bottom = 24.dp, end = 16.dp)
        )
    }

    ReportBlockBottomSheet(
        isVisible = isMenuBottomSheetVisible,
        onDismiss = { isMenuBottomSheetVisible = false },
        onReportClick = {
            isMenuBottomSheetVisible = false
            isReportUserDialogVisible = true
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

    ReportUserDialog(
        isVisible = isReportUserDialogVisible,
        onDismiss = { isReportUserDialogVisible = false },
        onReportClick = {
            isReportUserDialogVisible = false
            onReportUserClick()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun FeedProfileScreenPreview() {
    HilingualTheme {
        FeedProfileScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = FeedProfileUiState.Fake,
            onBackClick = {},
            onActionButtonClick = {},
            onReportUserClick = {},
            onBlockClick = {},
            onFollowClick = {},
            onProfileClick = {},
            onContentDetailClick = {},
            onLikeClick = { _, _, _ -> },
            onUnpublishClick = {},
            onReportDiaryClick = {},
            onTabRefresh = {}
        )
    }
}
