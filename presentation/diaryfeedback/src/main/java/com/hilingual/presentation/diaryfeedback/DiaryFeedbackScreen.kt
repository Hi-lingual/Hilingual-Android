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
package com.hilingual.presentation.diaryfeedback

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
import androidx.compose.runtime.mutableIntStateOf
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
import com.hilingual.core.common.analytics.Page.FEEDBACK
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalMessageController
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.dialog.diary.DiaryDeleteDialog
import com.hilingual.core.ui.component.dialog.diary.DiaryPublishDialog
import com.hilingual.core.ui.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.ui.component.item.diary.image.ModalImage
import com.hilingual.core.ui.component.item.diary.tab.GrammarSpellingTab
import com.hilingual.core.ui.component.item.diary.tab.RecommendExpressionTab
import com.hilingual.core.ui.component.item.diary.tabrow.DiaryTabRow
import com.hilingual.core.ui.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.presentation.diaryfeedback.component.FeedbackMenuBottomSheet
import com.hilingual.presentation.diaryfeedback.component.FeedbackReportDialog
import kotlinx.coroutines.launch

@Composable
internal fun DiaryFeedbackRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToFeed: () -> Unit,
    navigateToVoca: () -> Unit,
    viewModel: DiaryFeedbackViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var isImageDetailVisible by remember { mutableStateOf(false) }

    val dialogTrigger = LocalDialogTrigger.current
    val messageController = LocalMessageController.current
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
            is DiaryFeedbackSideEffect.ShowErrorDialog -> dialogTrigger.show(navigateUp)

            is DiaryFeedbackSideEffect.ShowDiaryPublishSnackbar -> {
                messageController(
                    HilingualMessage.Snackbar(
                        message = it.message,
                        actionLabelText = it.actionLabel,
                        onAction = {
                            tracker.logEvent(
                                trigger = TriggerType.CLICK,
                                page = FEEDBACK,
                                event = "toast_action",
                                properties = mapOf(
                                    "toast_id" to "diary_post_success",
                                    "toast_action" to "cta_click",
                                    "entry_id" to viewModel.diaryId,
                                    "page" to FEEDBACK.pageName
                                )
                            )
                            navigateToFeed()
                        }
                    )
                )
            }

            is DiaryFeedbackSideEffect.ShowVocaOverflowSnackbar -> {
                messageController(
                    HilingualMessage.Snackbar(
                        message = it.message,
                        actionLabelText = it.actionLabel,
                        onAction = navigateToVoca
                    )
                )
            }

            is DiaryFeedbackSideEffect.ShowToast -> messageController(HilingualMessage.Toast(it.message))

            is DiaryFeedbackSideEffect.NavigateToHome -> navigateToHome()
        }
    }

    LaunchedEffect(Unit) {
        tracker.logEvent(trigger = TriggerType.VIEW, page = FEEDBACK, event = "page")
    }

    DiaryFeedbackScreen(
        paddingValues = paddingValues,
        uiState = state,
        diaryId = viewModel.diaryId,
        onBackClick = {
            tracker.logEvent(
                trigger = TriggerType.CLICK,
                page = FEEDBACK,
                event = "back_feedback",
                properties = mapOf(
                    "entry_id" to viewModel.diaryId,
                    "back_source" to "ui_button"
                )
            )
            navigateUp()
        },
        onReportClick = { context.launchCustomTabs(UrlConstant.FEEDBACK_REPORT) },
        isImageDetailVisible = isImageDetailVisible,
        onChangeImageDetailVisible = { isImageDetailVisible = !isImageDetailVisible },
        onToggleIsPublished = { isPublished ->
            if (isPublished) {
                tracker.logEvent(
                    trigger = TriggerType.CLICK,
                    page = FEEDBACK,
                    event = "submitted_post_diary",
                    properties = mapOf("entry_id" to viewModel.diaryId)
                )
            }
            viewModel.toggleIsPublished(isPublished)
        },
        onToggleBookmark = viewModel::toggleBookmark,
        onDeleteDiary = viewModel::deleteDiary,
        tracker = tracker
    )
}

@Composable
private fun DiaryFeedbackScreen(
    paddingValues: PaddingValues,
    uiState: UiState<DiaryFeedbackUiState>,
    diaryId: Long,
    onBackClick: () -> Unit,
    onReportClick: () -> Unit,
    isImageDetailVisible: Boolean,
    onChangeImageDetailVisible: () -> Unit,
    onToggleIsPublished: (Boolean) -> Unit,
    onToggleBookmark: (Long, Boolean) -> Unit,
    onDeleteDiary: () -> Unit,
    tracker: Tracker,
    modifier: Modifier = Modifier
) {
    var isPublishDialogVisible by remember { mutableStateOf(false) }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }

    var isReportBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportDialogVisible by remember { mutableStateOf(false) }

    var isAIWrittenDiary by remember { mutableStateOf(true) }
    var toggleClickCount by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val grammarListState = rememberLazyListState()
    val recommendListState = rememberLazyListState()

    val successData = (uiState as? UiState.Success)?.data
    val isPublished = successData?.isPublished ?: false

    val isFabVisible by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> grammarListState.firstVisibleItemScrollOffset > 5
                else -> recommendListState.firstVisibleItemScrollOffset > 5
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0) {
            grammarListState.scrollToItem(0)
        } else {
            recommendListState.scrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarColor(HilingualTheme.colors.white)
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackAndMoreTopAppBar(
            title = "일기장",
            onBackClicked = onBackClick,
            onMoreClicked = { isReportBottomSheetVisible = true }
        )

        DiaryTabRow(
            tabIndex = pagerState.currentPage,
            onTabSelected = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

        when (uiState) {
            is UiState.Loading -> HilingualLoadingIndicator()

            is UiState.Success -> {
                val data = uiState.data

                Box(
                    modifier = Modifier
                        .background(HilingualTheme.colors.gray100)
                        .weight(1f)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> GrammarSpellingTab(
                                listState = grammarListState,
                                writtenDate = data.writtenDate,
                                diaryContent = data.diaryContent,
                                feedbackList = data.feedbackList,
                                isAIWrittenDiary = isAIWrittenDiary,
                                onImageClick = onChangeImageDetailVisible,
                                onToggleViewMode = {
                                    isAIWrittenDiary = it
                                    toggleClickCount++
                                    tracker.logEvent(
                                        trigger = TriggerType.CLICK,
                                        page = FEEDBACK,
                                        event = "toggle",
                                        properties = mapOf(
                                            "entry_id" to diaryId,
                                            "toggle_state" to it,
                                            "toggle_click_count" to toggleClickCount
                                        )
                                    )
                                }
                            )

                            1 -> RecommendExpressionTab(
                                listState = recommendListState,
                                writtenDate = data.writtenDate,
                                recommendExpressionList = data.recommendExpressionList,
                                onBookmarkClick = { phraseId, isMarked ->
                                    tracker.logEvent(
                                        trigger = TriggerType.CLICK,
                                        page = FEEDBACK,
                                        event = "bookmark_action",
                                        properties = mapOf(
                                            "entry_id" to diaryId,
                                            "bookmark_action" to if (isMarked) "add" else "remove",
                                            "page" to FEEDBACK.pageName,
                                            "tab_name" to "recommend_expression"
                                        )
                                    )
                                    onToggleBookmark(phraseId, isMarked)
                                }
                            )
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

                HilingualButton(
                    text = if (isPublished) "비공개하기" else "피드에 게시하기",
                    onClick = { isPublishDialogVisible = true },
                    modifier = Modifier
                        .background(HilingualTheme.colors.gray100)
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 16.dp)
                )
            }

            else -> {}
        }
    }

    if (isImageDetailVisible && successData?.diaryContent?.imageUrl != null) {
        ModalImage(
            imageUrl = successData.diaryContent.imageUrl ?: "",
            onBackClick = onChangeImageDetailVisible,
            modifier = modifier.padding(paddingValues)
        )
    }

    if (isPublished) {
        DiaryUnpublishDialog(
            isVisible = isPublishDialogVisible,
            onDismiss = { isPublishDialogVisible = false },
            onPrivateClick = {
                onToggleIsPublished(false)
                isPublishDialogVisible = false
            }
        )
    } else {
        DiaryPublishDialog(
            isVisible = isPublishDialogVisible,
            onDismiss = { isPublishDialogVisible = false },
            onPostClick = {
                onToggleIsPublished(true)
                isPublishDialogVisible = false
            }
        )
    }

    DiaryDeleteDialog(
        isVisible = isDeleteDialogVisible,
        onDismiss = { isDeleteDialogVisible = false },
        onDeleteClick = {
            isDeleteDialogVisible = false
            onDeleteDiary()
        }
    )

    FeedbackMenuBottomSheet(
        isVisible = isReportBottomSheetVisible,
        onDismiss = { isReportBottomSheetVisible = false },
        onDeleteClick = {
            isReportBottomSheetVisible = false
            isDeleteDialogVisible = true
        },
        onReportClick = {
            isReportBottomSheetVisible = false
            isReportDialogVisible = true
        }
    )

    FeedbackReportDialog(
        isVisible = isReportDialogVisible,
        onDismiss = { isReportDialogVisible = false },
        onReportClick = onReportClick
    )
}

@Preview(showBackground = true)
@Composable
private fun DiaryFeedbackScreenPreview() {
    HilingualTheme {
        DiaryFeedbackScreen(
            paddingValues = PaddingValues(),
            uiState = UiState.Success(
                DiaryFeedbackUiState.Fake
            ),
            diaryId = 0L,
            isImageDetailVisible = false,
            onChangeImageDetailVisible = {},
            onBackClick = {},
            onReportClick = {},
            onToggleBookmark = { _, _ -> {} },
            onToggleIsPublished = {},
            onDeleteDiary = {},
            tracker = FakeTracker()
        )
    }
}
