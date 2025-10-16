package com.hilingual.presentation.diarywrite.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.component.AnimatedLoadingLottie
import com.hilingual.presentation.diarywrite.component.AnimatedLoadingText
import com.hilingual.presentation.diarywrite.component.FeedbackLoadingContent
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DiaryFeedbackLoadingScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(HilingualTheme.colors.white)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "일기 저장 중..",
                color = HilingualTheme.colors.gray850,
                style = HilingualTheme.typography.headB20,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedLoadingText(
                texts = persistentListOf(
                    "피드백을 요청하고 있어요.",
                    "오늘 하루도 수고했어요!",
                    "발전하는 모습이 멋져요."
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedLoadingLottie(height = 194.dp)
        }

        FeedbackLoadingContent()
    }
}

@Preview
@Composable
private fun DiaryFeedbackLoadingScreenPreview() {
    HilingualTheme {
        DiaryFeedbackLoadingScreen(paddingValues = PaddingValues(0.dp))
    }
}
