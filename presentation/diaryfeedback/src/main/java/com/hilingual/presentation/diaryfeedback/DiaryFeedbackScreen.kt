package com.hilingual.presentation.diaryfeedback

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.button.HilingualFloatingButton
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.diaryfeedback.component.DiaryFeedbackTabRow
import com.hilingual.presentation.diaryfeedback.component.FeedbackReportBottomSheet
import com.hilingual.presentation.diaryfeedback.component.FeedbackReportDialog
import com.hilingual.presentation.diaryfeedback.tab.GrammarSpellingScreen
import com.hilingual.presentation.diaryfeedback.tab.RecommendExpressionScreen
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DiaryFeedbackRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: DiaryFeedbackViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val localSystemBarsColor = LocalSystemBarsColor.current
    var isImageDetailVisible by remember { mutableStateOf(false) }

    BackHandler {
        if (isImageDetailVisible) {
            isImageDetailVisible = false
        } else {
            navigateUp()
        }
    }

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = white
        )
    }

    when (state) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            DiaryFeedbackScreen(
                paddingValues = paddingValues,
                uiState = (state as UiState.Success<DiaryFeedbackUiState>).data,
                onBackClick = navigateUp,
                isImageDetailVisible = isImageDetailVisible,
                onChangeImageDetailVisible = { isImageDetailVisible = !isImageDetailVisible },
                onToggleBookmark = viewModel::toggleBookmark
            )
        }

        else -> { }
    }
}

@Composable
private fun DiaryFeedbackScreen(
    paddingValues: PaddingValues,
    uiState: DiaryFeedbackUiState,
    onBackClick: () -> Unit,
    isImageDetailVisible: Boolean,
    onChangeImageDetailVisible: () -> Unit,
    onToggleBookmark: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isReportBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportDialogVisible by remember { mutableStateOf(false) }
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

    if (isReportBottomSheetVisible) {
        FeedbackReportBottomSheet(
            onDismiss = { isReportBottomSheetVisible = false },
            onReportClick = {
                isReportDialogVisible = true
                isReportBottomSheetVisible = false
            }
        )
    }

    if (isReportDialogVisible) {
        FeedbackReportDialog(
            onDismiss = { isReportDialogVisible = false },
            onReportClick = {
                // TODO: 구글폼으로 이동
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize()
                .background(HilingualTheme.colors.white)
        ) {
            BackAndMoreTopAppBar(
                title = "일기장",
                onBackClicked = onBackClick,
                onMoreClicked = { isReportBottomSheetVisible = true }
            )

            DiaryFeedbackTabRow(
                tabIndex = pagerState.currentPage,
                onTabSelected = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

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
        }
        HilingualFloatingButton(
            onClick = {
                coroutineScope.launch {
                    when (pagerState.currentPage) {
                        0 -> grammarListState.animateScrollToItem(0)
                        else -> recommendListState.animateScrollToItem(0)
                    }
                }
            },
            isVisible = isFabVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp)
        )
    }

    if (isImageDetailVisible && uiState.diaryContent.imageUrl != null) {
        ModalImage(
            imageUrl = uiState.diaryContent.imageUrl,
            onBackClick = onChangeImageDetailVisible,
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryFeedbackScreenPreview() {
    HilingualTheme {
        DiaryFeedbackScreen(
            paddingValues = PaddingValues(),
            uiState = DiaryFeedbackUiState(
                writtenDate = "7월 11일 금요일",
                feedbackList = persistentListOf(),
                recommendExpressionList = persistentListOf()
            ),
            isImageDetailVisible = false,
            onChangeImageDetailVisible = {},
            onBackClick = {},
            onToggleBookmark = { _, _ -> {} }
        )
    }
}
