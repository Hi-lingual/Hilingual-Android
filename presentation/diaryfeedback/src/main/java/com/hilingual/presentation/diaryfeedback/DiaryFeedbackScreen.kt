package com.hilingual.presentation.diaryfeedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.topappbar.BackAndMoreTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

//TODO: Route 추가

@Composable
private fun DiaryFeedbackScreen(
    modifier: Modifier = Modifier
) {
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
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryFeedbackPreview() {
    HilingualTheme {
        DiaryFeedbackScreen()
    }
}