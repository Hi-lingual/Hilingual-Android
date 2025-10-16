/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.feedprofile.profile

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.model.SnackbarRequest
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalSnackbarTrigger
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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
internal fun FeedProfileRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: (Boolean) -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToFollowList: () -> Unit,
    navigateToFeedDiary: (Long) -> Unit,
    viewModel: FeedProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackbarTrigger = LocalSnackbarTrigger.current
    val toastTrigger = LocalToastTrigger.current
    val dialogTrigger = LocalDialogTrigger.current

    LaunchedEffect(Unit) {
        viewModel.loadFeedProfile()
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FeedProfileSideEffect.ShowDiaryLikeSnackbar -> {
                snackbarTrigger(
                    SnackbarRequest(
                        message = it.message,
                        buttonText = it.actionLabel,
                        onClick = { navigateToMyFeedProfile(true) }
                    )
                )
            }

            is FeedProfileSideEffect.ShowToast -> {
                toastTrigger(it.message)
            }

            is FeedProfileSideEffect.ShowErrorDialog -> dialogTrigger.show(navigateUp)
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
                initialTab = if (viewModel.showLikedDiaries) 1 else 0,
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
    initialTab: Int,
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
    val pagerState = rememberPagerState(initialPage = initialTab, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var isMenuBottomSheetVisible by remember { mutableStateOf(false) }
    var isBlockBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportUserDialogVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val sharedDiaryListState = rememberLazyListState()
    val likedDiaryListState = rememberLazyListState()

    val currentListState by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> sharedDiaryListState
                else -> likedDiaryListState
            }
        }
    }

    val profile = uiState.feedProfileInfo

    val isFabVisible by remember {
        derivedStateOf {
            scrollState.value > 0 || currentListState.firstVisibleItemIndex > 0 || currentListState.firstVisibleItemScrollOffset > 0
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .drop(1)
            .collect { pageIndex ->
                val tabType = if (pageIndex == 0) DiaryTabType.SHARED else DiaryTabType.LIKED
                onTabRefresh(tabType)
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                    when {
                        profile.isMine -> {
                            currentListState.animateScrollToItem(0)
                        }
                        profile.isBlock != true -> {
                            sharedDiaryListState.animateScrollToItem(0)
                        }
                    }
                }
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
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
                    FeedProfileTabRow(
                        tabIndex = pagerState.currentPage,
                        onTabSelected = { index ->
                            coroutineScope.launch {
                                val tabType = if (index == 0) DiaryTabType.SHARED else DiaryTabType.LIKED
                                if (index == pagerState.currentPage) {
                                    onTabRefresh(tabType)
                                } else {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(HilingualTheme.colors.white)
                            .stickyHeader(scrollState)
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val tabType = DiaryTabType.entries[page]
                        val (diaries, emptyCardType, listState) = when (tabType) {
                            DiaryTabType.SHARED -> Triple(
                                uiState.sharedDiaries,
                                FeedEmptyCardType.NOT_SHARED,
                                sharedDiaryListState
                            )
                            DiaryTabType.LIKED -> Triple(
                                uiState.likedDiaries,
                                FeedEmptyCardType.NOT_LIKED,
                                likedDiaryListState
                            )
                        }

                        DiaryListScreen(
                            diaries = diaries,
                            emptyCardType = emptyCardType,
                            onProfileClick = onProfileClick,
                            onContentDetailClick = onContentDetailClick,
                            onLikeClick = { diaryId, isLiked -> onLikeClick(diaryId, isLiked, tabType) },
                            onUnpublishClick = onUnpublishClick,
                            onReportClick = onReportDiaryClick,
                            listState = listState,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                profile.isBlock == true -> {
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

                else -> {
                    DiaryListScreen(
                        diaries = uiState.sharedDiaries,
                        emptyCardType = FeedEmptyCardType.NOT_SHARED,
                        onProfileClick = onProfileClick,
                        onContentDetailClick = onContentDetailClick,
                        onLikeClick = { diaryId, isLiked -> onLikeClick(diaryId, isLiked, DiaryTabType.SHARED) },
                        onUnpublishClick = onUnpublishClick,
                        onReportClick = onReportDiaryClick,
                        listState = sharedDiaryListState,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        HilingualFloatingButton(
            isVisible = isFabVisible,
            onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                    when {
                        profile.isMine -> {
                            currentListState.animateScrollToItem(0)
                        }
                        profile.isBlock != true -> {
                            sharedDiaryListState.animateScrollToItem(0)
                        }
                    }
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

private fun Modifier.stickyHeader(scrollState: ScrollState): Modifier = composed {
    var initialY by remember { mutableFloatStateOf(0f) }
    var isPlaced by remember { mutableStateOf(false) }
    this
        .onGloballyPositioned { coordinates ->
            if (!isPlaced) {
                initialY = coordinates.parentLayoutCoordinates?.localPositionOf(
                    coordinates,
                    Offset.Zero
                )?.y ?: 0f
                isPlaced = true
            }
        }
        .graphicsLayer {
            translationY = if (scrollState.value > initialY) {
                scrollState.value - initialY
            } else {
                0f
            }
        }
}

@Preview(showBackground = true)
@Composable
private fun FeedProfileScreenPreview() {
    HilingualTheme {
        FeedProfileScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = FeedProfileUiState.Fake,
            initialTab = 0,
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
