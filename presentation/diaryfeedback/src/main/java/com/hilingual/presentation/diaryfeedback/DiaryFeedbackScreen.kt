package com.hilingual.presentation.diaryfeedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.diaryfeedback.component.DiaryFeedbackTabRow
import com.hilingual.presentation.diaryfeedback.tab.GrammarSpellingScreen
import com.hilingual.presentation.diaryfeedback.tab.RecommendExpressionScreen

//TODO: Route 설정
@Composable
internal fun DiaryFeedbackRoute(
    paddingValues: PaddingValues,
    viewModel: DiaryFeedbackViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = white,
            darkIcons = false
        )
    }

    when (val state = uiState) {
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
                uiState = state.data,
                onToggleDiaryViewMode = viewModel::toggleDiaryShowOption,
                onToggleBookmark = viewModel::toggleBookmark
            )
        }

        else -> { }
    }
}

@Composable
private fun DiaryFeedbackScreen(
    uiState: DiaryFeedbackUiState,
    onToggleDiaryViewMode: (Boolean) -> Unit,
    onToggleBookmark: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var tabIndex by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.white)
    ) {
        BackAndMoreTopAppBar(
            title = "일기장",
            onBackClicked = {
                //TODO: 뒤로가기 동작 추가
            },
            onMoreClicked = {
                //TODO: AI 신고하기 바텀시트 노출
            }
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
                    onToggleDiaryViewMode = onToggleDiaryViewMode,
                    isAIDiary = isAI
                )
                1 -> RecommendExpressionScreen(
                    writtenDate = writtenDate,
                    recommendExpressionList = recommendExpressionList,
                    isBookmarkClick = onToggleBookmark,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryFeedbackScreenPreview() {
    HilingualTheme {
        val vm = DiaryFeedbackViewModel
        var isAI by remember { mutableStateOf(true) }

        DiaryFeedbackScreen(
            uiState = DiaryFeedbackUiState(
                isAI = isAI,
                diaryContent = vm.dummyDiaryContent,
                feedbackList = vm.dummyFeedbacks,
                recommendExpressionList = vm.dummyRecommendExpressions
            ),
            onToggleDiaryViewMode = { isAI = !isAI },
            onToggleBookmark = { _, _ -> {} }
        )
    }
}