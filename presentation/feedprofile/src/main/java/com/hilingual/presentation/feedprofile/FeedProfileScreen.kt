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
package com.hilingual.presentation.feedprofile

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.analytics.Page
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.dialog.report.ReportUserDialog
import com.hilingual.core.ui.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.ui.component.topappbar.BackTopAppBar
import com.hilingual.presentation.feedprofile.component.bottomsheet.BlockBottomSheet
import com.hilingual.presentation.feedprofile.component.bottomsheet.ReportBlockBottomSheet
import com.hilingual.presentation.feedprofile.component.card.FeedEmptyCardType
import com.hilingual.presentation.feedprofile.component.card.FeedProfileInfo
import com.hilingual.presentation.feedprofile.component.tab.DiaryListScreen
import com.hilingual.presentation.feedprofile.component.tab.FeedProfileTabRow
import com.hilingual.presentation.feedprofile.model.DiaryTabType
import com.hilingual.presentation.feedprofile.model.FeedDiaryUIModel
import com.hilingual.presentation.feedprofile.model.FeedProfileInfoModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
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

    val messageController = LocalMessageController.current
    val dialogTrigger = LocalDialogTrigger.current
    val tracker = LocalTracker.current

    LaunchedEffect(Unit) {
        tracker.logEvent(
            trigger = TriggerType.VIEW,
            page = Page.FEED,
            event = "view_profile_user",
            properties = mapOf(
                "profile_user_id" to viewModel.targetUserId,
                "page" to Page.FEED.pageName
            )
        )
        viewModel.loadFeedProfile()
    }

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is FeedProfileSideEffect.NavigateToFollowList -> navigateToFollowList()

            is FeedProfileSideEffect.ShowDiaryLikeSnackbar ->
                messageController(
                    HilingualMessage.Snackbar(
                        message = sideEffect.message,
                        actionLabelText = sideEffect.actionLabel,
                        onAction = { navigateToMyFeedProfile(true) }
                    )
                )

            is FeedProfileSideEffect.ShowToast -> messageController(HilingualMessage.Toast(sideEffect.message))

            is FeedProfileSideEffect.ShowErrorDialog -> dialogTrigger.show(onClick = navigateUp)
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
                onFollowClick = { viewModel.onFollowClick() },
                onActionButtonClick = viewModel::onActionButtonClick,
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
    onActionButtonClick: () -> Unit,
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
    val profile = uiState.feedProfileInfo

    val pagerState = rememberPagerState(initialPage = initialTab, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var isMenuBottomSheetVisible by remember { mutableStateOf(false) }
    var isBlockBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportUserDialogVisible by remember { mutableStateOf(false) }

    val sharedDiaryListState = rememberLazyListState()
    val likedDiaryListState = rememberLazyListState()

    val currentListState by remember {
        derivedStateOf {
            when {
                profile.isMine -> {
                    if (pagerState.currentPage == 0) sharedDiaryListState else likedDiaryListState
                }
                else -> sharedDiaryListState
            }
        }
    }

    val isFabVisible by remember {
        derivedStateOf {
            currentListState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .drop(1)
            .collect { pageIndex ->
                val tabType = if (pageIndex == 0) DiaryTabType.SHARED else DiaryTabType.LIKED
                onTabRefresh(tabType)
                scrollToTop(currentListState, coroutineScope)
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
            FeedProfileTopBar(
                isMine = profile.isMine,
                isBlock = profile.isBlock,
                onBackClick = onBackClick,
                onMoreClick = { isMenuBottomSheetVisible = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FeedProfileInfo(
                profileImageUrl = profile.profileImageUrl,
                nickname = profile.nickname,
                streak = profile.streak,
                follower = profile.follower,
                following = profile.following,
                onFollowClick = { onFollowClick(profile.isMine) },
                isMine = profile.isMine,
                isFollowing = profile.isFollowing,
                isFollowed = profile.isFollowed,
                isBlock = profile.isBlock,
                onActionButtonClick = onActionButtonClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            FeedProfileContent(
                profile = profile,
                sharedDiaries = uiState.sharedDiaries,
                likedDiaries = uiState.likedDiaries,
                pagerState = pagerState,
                sharedDiaryListState = sharedDiaryListState,
                likedDiaryListState = likedDiaryListState,
                coroutineScope = coroutineScope,
                onProfileClick = onProfileClick,
                onContentDetailClick = onContentDetailClick,
                onLikeClick = onLikeClick,
                onUnpublishClick = onUnpublishClick,
                onReportDiaryClick = onReportDiaryClick,
                onTabRefresh = onTabRefresh,
                modifier = Modifier.fillMaxSize()
            )
        }

        HilingualFloatingButton(
            isVisible = isFabVisible,
            onClick = { scrollToTop(currentListState, coroutineScope) },
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

@Composable
private fun FeedProfileTopBar(
    isMine: Boolean,
    isBlock: Boolean?,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    if (isMine || isBlock == true) {
        BackTopAppBar(title = "피드", onBackClicked = onBackClick)
    } else {
        BackAndMoreTopAppBar(
            title = "피드",
            onBackClicked = onBackClick,
            onMoreClicked = onMoreClick
        )
    }
}

@Composable
private fun FeedProfileContent(
    profile: FeedProfileInfoModel,
    sharedDiaries: ImmutableList<FeedDiaryUIModel>,
    likedDiaries: ImmutableList<FeedDiaryUIModel>,
    pagerState: PagerState,
    sharedDiaryListState: LazyListState,
    likedDiaryListState: LazyListState,
    coroutineScope: CoroutineScope,
    onProfileClick: (Long) -> Unit,
    onContentDetailClick: (Long) -> Unit,
    onLikeClick: (Long, Boolean, DiaryTabType) -> Unit,
    onUnpublishClick: (Long) -> Unit,
    onReportDiaryClick: () -> Unit,
    onTabRefresh: (DiaryTabType) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        profile.isMine -> {
            MyFeedContent(
                sharedDiaries = sharedDiaries,
                likedDiaries = likedDiaries,
                pagerState = pagerState,
                sharedDiaryListState = sharedDiaryListState,
                likedDiaryListState = likedDiaryListState,
                coroutineScope = coroutineScope,
                onProfileClick = onProfileClick,
                onContentDetailClick = onContentDetailClick,
                onLikeClick = onLikeClick,
                onUnpublishClick = onUnpublishClick,
                onReportDiaryClick = onReportDiaryClick,
                onTabRefresh = onTabRefresh,
                modifier = modifier
            )
        }

        profile.isBlock == true -> {
            BlockedUserContent(
                nickname = profile.nickname,
                modifier = modifier
            )
        }

        else -> {
            OtherUserFeedContent(
                diaries = sharedDiaries,
                listState = sharedDiaryListState,
                onProfileClick = onProfileClick,
                onContentDetailClick = onContentDetailClick,
                onLikeClick = onLikeClick,
                onUnpublishClick = onUnpublishClick,
                onReportDiaryClick = onReportDiaryClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun MyFeedContent(
    sharedDiaries: ImmutableList<FeedDiaryUIModel>,
    likedDiaries: ImmutableList<FeedDiaryUIModel>,
    pagerState: PagerState,
    sharedDiaryListState: LazyListState,
    likedDiaryListState: LazyListState,
    coroutineScope: CoroutineScope,
    onProfileClick: (Long) -> Unit,
    onContentDetailClick: (Long) -> Unit,
    onLikeClick: (Long, Boolean, DiaryTabType) -> Unit,
    onUnpublishClick: (Long) -> Unit,
    onReportDiaryClick: () -> Unit,
    onTabRefresh: (DiaryTabType) -> Unit,
    modifier: Modifier = Modifier
) {
    FeedProfileTabRow(
        tabIndex = pagerState.currentPage,
        onTabSelected = { index ->
            coroutineScope.launch {
                val tabType = if (index == 0) DiaryTabType.SHARED else DiaryTabType.LIKED
                if (index == pagerState.currentPage) {
                    onTabRefresh(tabType)
                    val currentListState = if (index == 0) sharedDiaryListState else likedDiaryListState
                    scrollToTop(currentListState, coroutineScope)
                } else {
                    pagerState.animateScrollToPage(index)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(HilingualTheme.colors.white)
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val tabType = DiaryTabType.entries[page]
        val (diaries, emptyCardType, listState) = when (tabType) {
            DiaryTabType.SHARED -> Triple(
                sharedDiaries,
                FeedEmptyCardType.NOT_SHARED,
                sharedDiaryListState
            )

            DiaryTabType.LIKED -> Triple(
                likedDiaries,
                FeedEmptyCardType.NOT_LIKED,
                likedDiaryListState
            )
        }

        DiaryListScreen(
            diaries = diaries,
            emptyCardType = emptyCardType,
            onProfileClick = onProfileClick,
            onContentDetailClick = onContentDetailClick,
            onLikeClick = { diaryId, isLiked ->
                onLikeClick(diaryId, isLiked, tabType)
            },
            onUnpublishClick = onUnpublishClick,
            onReportClick = onReportDiaryClick,
            listState = listState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun BlockedUserContent(
    nickname: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(140.dp))

        Text(
            text = "${nickname}님의 글을 확인할 수 없어요.",
            style = HilingualTheme.typography.headSB18,
            color = HilingualTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "차단을 해제하면 글을 확인할 수 있어요.",
            style = HilingualTheme.typography.bodyR16,
            color = HilingualTheme.colors.gray400,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OtherUserFeedContent(
    diaries: ImmutableList<FeedDiaryUIModel>,
    listState: LazyListState,
    onProfileClick: (Long) -> Unit,
    onContentDetailClick: (Long) -> Unit,
    onLikeClick: (Long, Boolean, DiaryTabType) -> Unit,
    onUnpublishClick: (Long) -> Unit,
    onReportDiaryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DiaryListScreen(
        diaries = diaries,
        emptyCardType = FeedEmptyCardType.NOT_SHARED,
        onProfileClick = onProfileClick,
        onContentDetailClick = onContentDetailClick,
        onLikeClick = { diaryId, isLiked ->
            onLikeClick(diaryId, isLiked, DiaryTabType.SHARED)
        },
        onUnpublishClick = onUnpublishClick,
        onReportClick = onReportDiaryClick,
        listState = listState,
        modifier = modifier
    )
}

private fun scrollToTop(listState: LazyListState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        listState.animateScrollToItem(0)
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
