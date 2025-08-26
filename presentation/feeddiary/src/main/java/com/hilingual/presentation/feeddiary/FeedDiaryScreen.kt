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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.constant.UrlConstant
import com.hilingual.core.common.extension.launchCustomTabs
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.bottomsheet.BlockBottomSheet
import com.hilingual.core.designsystem.component.bottomsheet.ReportBlockBottomSheet
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.content.diary.DiaryTabRow
import com.hilingual.core.designsystem.component.content.diary.ModalImage
import com.hilingual.core.designsystem.component.dialog.diary.DiaryUnpublishDialog
import com.hilingual.core.designsystem.component.indicator.HilingualLoadingIndicator
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.feeddiary.component.DiaryUnpublishBottomSheet
import com.hilingual.presentation.feeddiary.component.FeedDiaryProfile
import com.hilingual.presentation.feeddiary.model.ProfileContentUiModel
import com.hilingual.presentation.feeddiary.tab.GrammarSpellingScreen
import com.hilingual.presentation.feeddiary.tab.RecommendExpressionScreen
import kotlinx.coroutines.launch

@Composable
internal fun FeedDiaryRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: FeedDiaryViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isImageDetailVisible by remember { mutableStateOf(false) }

    BackHandler {
        if (isImageDetailVisible) {
            isImageDetailVisible = false
        } else {
            navigateUp()
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> HilingualLoadingIndicator()

        is UiState.Success -> {
            FeedDiaryScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onBackClick = navigateUp,
                onProfileClick = {},
                onLikeClick = {},
                onPrivateClick = {},
                onBlockClick = {},
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
    onProfileClick: () -> Unit,
    onLikeClick: () -> Unit,
    onPrivateClick: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    isImageDetailVisible: Boolean,
    onChangeImageDetailVisible: () -> Unit,
    onToggleBookmark: (Long, Boolean) -> Unit
) {
    var isUnpublishBottomSheetVisible by remember { mutableStateOf(false) }
    var isUnpublishDialogVisible by remember { mutableStateOf(false) }

    var isReportBottomSheetVisible by remember { mutableStateOf(false) }
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
            title = null
        )

        with(uiState.profileContent) {
            FeedDiaryProfile(
                profileUrl = profileUrl,
                nickname = nickname,
                streak = streak,
                isLiked = isLiked,
                likeCount = likeCount,
                sharedDateInMinutes = sharedDateInMinutes,
                onProfileClick = onProfileClick,
                onLikeClick = onLikeClick
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
                        0 -> GrammarSpellingScreen(
                            listState = grammarListState,
                            writtenDate = writtenDate,
                            diaryContent = diaryContent,
                            feedbackList = feedbackList,
                            onImageClick = onChangeImageDetailVisible
                        )

                        1 -> RecommendExpressionScreen(
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
            imageUrl = uiState.diaryContent.imageUrl,
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
                onReportClick()
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
            // TODO: 비공개 후 이전 화면으로 이동, 토스트 표시
        }
    )

    BlockBottomSheet(
        isVisible = isBlockConfirmBottomSheetVisible,
        onDismiss = { isBlockConfirmBottomSheetVisible = false },
        onBlockButtonClick = {
            isBlockConfirmBottomSheetVisible = false
            onBlockClick()
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
            onProfileClick = {},
            onLikeClick = {},
            isImageDetailVisible = isImageDetailVisible,
            onChangeImageDetailVisible = { isImageDetailVisible = !isImageDetailVisible },
            onToggleBookmark = { _, _ -> },
            onPrivateClick = {},
            onReportClick = {},
            onBlockClick = {},
            uiState = FeedDiaryUiState(
                isMine = false,
                writtenDate = "8월 23일 토요일",
                profileContent = ProfileContentUiModel(
                    profileUrl = "",
                    nickname = "작나",
                    streak = 2,
                    isLiked = true,
                    likeCount = 112,
                    sharedDateInMinutes = 3
                )
            )
        )
    }
}
