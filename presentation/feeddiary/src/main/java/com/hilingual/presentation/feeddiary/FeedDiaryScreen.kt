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
package com.hilingual.presentation.feeddiary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.analytics.FakeTracker
import com.hilingual.core.common.analytics.Page
import com.hilingual.core.common.analytics.Tracker
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
import com.hilingual.core.ui.component.bottomsheet.BlockBottomSheet
import com.hilingual.core.ui.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.ui.component.dialog.report.ReportPostDialog
import com.hilingual.core.ui.component.item.diary.image.ModalImage
import com.hilingual.core.ui.component.item.diary.tab.GrammarSpellingTab
import com.hilingual.core.ui.component.item.diary.tab.RecommendExpressionTab
import com.hilingual.core.ui.component.item.diary.tabrow.DiaryTabRow
import com.hilingual.core.ui.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.presentation.feeddiary.component.DiaryUnpublishBottomSheet
import com.hilingual.presentation.feeddiary.component.FeedDiaryProfile
import com.hilingual.presentation.feeddiary.component.ReportBlockBottomSheet
import kotlinx.coroutines.launch

@Composable
internal fun FeedDiaryRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: (showLikedDiaries: Boolean) -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToVoca: () -> Unit,
    viewModel: FeedDiaryViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isImageDetailVisible by remember { mutableStateOf(false) }

    val messageController = LocalMessageController.current
    val dialogTrigger = LocalDialogTrigger.current
    val tracker = LocalTracker.current

    BackHandler {
        if (isImageDetailVisible) {
            isImageDetailVisible = false
        } else {
            navigateUp()
        }
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FeedDiarySideEffect.NavigateToUp -> navigateUp()

            is FeedDiarySideEffect.NavigateToFeedProfile -> navigateToFeedProfile(it.userId)

            is FeedDiarySideEffect.ShowVocaOverflowSnackbar -> {
                messageController(
                    HilingualMessage.Snackbar(
                        message = it.message,
                        actionLabelText = it.actionLabel,
                        onAction = navigateToVoca
                    )
                )
            }

            is FeedDiarySideEffect.ShowDiaryLikeSnackbar -> {
                messageController(
                    HilingualMessage.Snackbar(
                        message = it.message,
                        actionLabelText = it.actionLabel,
                        onAction = { navigateToMyFeedProfile(true) }
                    )
                )
            }

            is FeedDiarySideEffect.ShowToast -> {
                messageController(HilingualMessage.Toast(it.message))
            }

            is FeedDiarySideEffect.ShowErrorDialog -> {
                dialogTrigger.show(navigateUp)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> HilingualLoadingIndicator()

        is UiState.Success -> {
            FeedDiaryScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                diaryId = viewModel.diaryId,
                tracker = tracker,
                onBackClick = navigateUp,
                onMyProfileClick = navigateToMyFeedProfile,
                onProfileClick = navigateToFeedProfile,
                onLikeClick = viewModel::toggleIsLiked,
                onPrivateClick = viewModel::diaryUnpublish,
                onBlockClick = viewModel::blockUser,
                onReportClick = { context.launchCustomTabs(UrlConstant.FEEDBACK_REPORT) },
                isImageDetailVisible = isImageDetailVisible,
                onChangeImageDetailVisible = { isImageDetailVisible = !isImageDetailVisible },
                onToggleBookmark = viewModel::toggleBookmark
            )
        }

        else -> {}
    }
}

@Composable
private fun FeedDiaryScreen(
    paddingValues: PaddingValues,
    uiState: FeedDiaryUiState,
    diaryId: Long,
    tracker: Tracker,
    onBackClick: () -> Unit,
    onMyProfileClick: (showLikedDiaries: Boolean) -> Unit,
    onProfileClick: (Long) -> Unit,
    onLikeClick: (Boolean) -> Unit,
    onPrivateClick: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: (Long) -> Unit,
    isImageDetailVisible: Boolean,
    onChangeImageDetailVisible: () -> Unit,
    onToggleBookmark: (Long, Boolean) -> Unit
) {
    var isUnpublishBottomSheetVisible by remember { mutableStateOf(false) }
    var isUnpublishDialogVisible by remember { mutableStateOf(false) }

    var isReportBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportConfirmDialogVisible by remember { mutableStateOf(false) }
    var isBlockConfirmBottomSheetVisible by remember { mutableStateOf(false) }

    var isAIWrittenDiary by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val grammarListState = rememberLazyListState()
    val recommendListState = rememberLazyListState()

    val isFabVisible by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> grammarListState.firstVisibleItemScrollOffset > 5
                else -> recommendListState.firstVisibleItemScrollOffset > 5
            }
        }
    }

    LaunchedEffect(Unit) {
        tracker.logEvent(
            trigger = TriggerType.VIEW,
            page = Page.POSTED_DIARY,
            event = "page",
            properties = mapOf(
                "entry_id" to diaryId,
                "tab_name" to if (pagerState.currentPage == 0) "grammar_spelling" else "recommend_expression"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackAndMoreTopAppBar(
            onBackClicked = onBackClick,
            onMoreClicked = {
                if (uiState.isMine) {
                    isUnpublishBottomSheetVisible = true
                } else {
                    isReportBottomSheetVisible = true
                }
            },
            title = "피드"
        )

        with(uiState) {
            FeedDiaryProfile(
                profileUrl = profileContent.profileUrl,
                nickname = profileContent.nickname,
                streak = profileContent.streak,
                isLiked = profileContent.isLiked,
                likeCount = profileContent.likeCount,
                sharedDateInMinutes = profileContent.sharedDateInMinutes,
                onProfileClick = {
                    if (isMine) {
                        onMyProfileClick(false)
                    } else {
                        onProfileClick(profileContent.userId)
                    }
                },
                onLikeClick = { onLikeClick(!profileContent.isLiked) }
            )
        }

        DiaryTabRow(
            tabIndex = pagerState.currentPage,
            onTabSelected = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

        Box(
            modifier = Modifier
                .background(HilingualTheme.colors.gray100)
                .weight(1f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                with(uiState) {
                    when (page) {
                        0 -> GrammarSpellingTab(
                            listState = grammarListState,
                            writtenDate = writtenDate,
                            diaryContent = diaryContent,
                            feedbackList = feedbackList,
                            isAIWrittenDiary = isAIWrittenDiary,
                            onImageClick = onChangeImageDetailVisible,
                            onToggleViewMode = { isAIWrittenDiary = it }
                        )

                        1 -> RecommendExpressionTab(
                            listState = recommendListState,
                            writtenDate = writtenDate,
                            recommendExpressionList = recommendExpressionList,
                            onBookmarkClick = onToggleBookmark
                        )
                    }
                }
            }

            HilingualFloatingButton(
                isVisible = isFabVisible,
                onClick = {
                    coroutineScope.launch {
                        when (pagerState.currentPage) {
                            0 -> grammarListState.animateScrollToItem(0)
                            else -> recommendListState.animateScrollToItem(0)
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 16.dp)
            )
        }
    }

    if (isImageDetailVisible && uiState.diaryContent.imageUrl != null) {
        ModalImage(
            imageUrl = uiState.diaryContent.imageUrl ?: "",
            onBackClick = onChangeImageDetailVisible,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (uiState.isMine) {
        DiaryUnpublishBottomSheet(
            isVisible = isUnpublishBottomSheetVisible,
            onDismiss = { isUnpublishBottomSheetVisible = false },
            onPrivateClick = {
                isUnpublishBottomSheetVisible = false
                isUnpublishDialogVisible = true
            }
        )
    } else {
        ReportBlockBottomSheet(
            isVisible = isReportBottomSheetVisible,
            onDismiss = { isReportBottomSheetVisible = false },
            onReportClick = {
                isReportBottomSheetVisible = false
                isReportConfirmDialogVisible = true
            },
            onBlockClick = {
                isReportBottomSheetVisible = false
                isBlockConfirmBottomSheetVisible = true
            }
        )
    }

    DiaryUnpublishDialog(
        isVisible = isUnpublishDialogVisible,
        onDismiss = { isUnpublishDialogVisible = false },
        onPrivateClick = {
            isUnpublishDialogVisible = false
            onPrivateClick()
        }
    )

    BlockBottomSheet(
        isVisible = isBlockConfirmBottomSheetVisible,
        onDismiss = { isBlockConfirmBottomSheetVisible = false },
        onBlockButtonClick = {
            isBlockConfirmBottomSheetVisible = false
            onBlockClick(uiState.profileContent.userId)
        }
    )

    ReportPostDialog(
        isVisible = isReportConfirmDialogVisible,
        onDismiss = { isReportConfirmDialogVisible = false },
        onReportClick = {
            isReportConfirmDialogVisible = false
            onReportClick()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun FeedDiaryScreenPreview() {
    var isImageDetailVisible by remember { mutableStateOf(false) }

    HilingualTheme {
        FeedDiaryScreen(
            paddingValues = PaddingValues(),
            diaryId = 0L,
            tracker = FakeTracker(),
            onBackClick = {},
            onMyProfileClick = {},
            onProfileClick = {},
            onLikeClick = {},
            isImageDetailVisible = isImageDetailVisible,
            onChangeImageDetailVisible = { isImageDetailVisible = !isImageDetailVisible },
            onToggleBookmark = { _, _ -> },
            onPrivateClick = {},
            onReportClick = {},
            onBlockClick = {},
            uiState = FeedDiaryUiState.Fake
        )
    }
}
