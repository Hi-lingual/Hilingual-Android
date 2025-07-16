package com.hilingual.presentation.diaryfeedback

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.provider.LocalSystemBarsColor
import com.hilingual.core.common.util.UiState
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
    var isImageDetailVisible by remember { mutableStateOf<Boolean>(false) }

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
    var tabIndex by remember { mutableIntStateOf(0) }
    var isReportBottomSheetVisible by remember { mutableStateOf(false) }
    var isReportDialogVisible by remember { mutableStateOf(false) }

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

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
    ) {
        BackAndMoreTopAppBar(
            title = "일기장",
            onBackClicked = onBackClick,
            onMoreClicked = { isReportBottomSheetVisible = true }
        )

        DiaryFeedbackTabRow(
            tabIndex = tabIndex,
            onTabSelected = { tabIndex = it }
        )

        with(uiState) {
            when (tabIndex) {
                0 -> GrammarSpellingScreen(
                    writtenDate = writtenDate,
                    diaryContent = diaryContent,
                    feedbackList = feedbackList,
                    onImageClick = onChangeImageDetailVisible
                )
                1 -> RecommendExpressionScreen(
                    writtenDate = writtenDate,
                    recommendExpressionList = recommendExpressionList,
                    onBookmarkClick = onToggleBookmark
                )
            }
        }
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
