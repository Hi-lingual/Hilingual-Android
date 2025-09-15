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
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.model.SnackbarRequest
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalSnackbarTrigger
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.bottomsheet.BlockBottomSheet
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.content.diary.DiaryTabRow
import com.hilingual.core.designsystem.component.content.diary.GrammarSpellingTab
import com.hilingual.core.designsystem.component.content.diary.ModalImage
import com.hilingual.core.designsystem.component.content.diary.RecommendExpressionTab
import com.hilingual.core.designsystem.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.designsystem.component.dialog.report.ReportPostDialog
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feeddiary.component.DiaryUnpublishBottomSheet
import com.hilingual.presentation.feeddiary.component.FeedDiaryProfile
import com.hilingual.presentation.feeddiary.component.ReportBlockBottomSheet
import kotlinx.coroutines.launch

@Composable
internal fun FeedDiaryRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToMyFeedProfile: () -> Unit,
    navigateToFeedProfile: (Long) -> Unit,
    navigateToVoca: () -> Unit,
    viewModel: FeedDiaryViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isImageDetailVisible by remember { mutableStateOf(false) }

    val snackbarTrigger = LocalSnackbarTrigger.current
    val toastTrigger = LocalToastTrigger.current
    val dialogTrigger = LocalDialogTrigger.current

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
                snackbarTrigger(
                    SnackbarRequest(
                        message = it.message,
                        buttonText = it.actionLabel,
                        onClick = navigateToVoca
                    )
                )
            }

            is FeedDiarySideEffect.ShowToast -> {
                toastTrigger(it.message)
            }

            is FeedDiarySideEffect.ShowErrorDialog -> {
                dialogTrigger.show(navigateUp)
            }

            is FeedDiarySideEffect.ShowErrorDialog -> dialogTrigger.show(navigateUp)
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> HilingualLoadingIndicator()

        is UiState.Success -> {
            FeedDiaryScreen(
                paddingValues = paddingValues,
                uiState = state.data,
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
    onBackClick: () -> Unit,
    onMyProfileClick: () -> Unit,
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

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0) {
            grammarListState.scrollToItem(0)
        } else {
            recommendListState.scrollToItem(0)
        }
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
                        onMyProfileClick()
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
                            onImageClick = onChangeImageDetailVisible
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
