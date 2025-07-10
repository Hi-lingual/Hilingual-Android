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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                onToggle = viewModel::toggleDiaryShowOption
            )
        }

        else -> { }
    }
}

@Composable
private fun DiaryFeedbackScreen(
    uiState: DiaryFeedbackUiState,
    onToggle: (Boolean) -> Unit,
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

        when (tabIndex) {
            0 -> uiState.apply {
                GrammarSpellingScreen(
                    diaryContent = diaryContent,
                    feedbackList = feedbackList,
                    onToggle = onToggle,
                    isAI = isAI
                )
            }
            1 -> RecommendExpressionScreen()
        }
    }
}