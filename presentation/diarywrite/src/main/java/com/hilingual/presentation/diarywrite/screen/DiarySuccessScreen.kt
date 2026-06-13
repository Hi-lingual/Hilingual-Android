package com.hilingual.presentation.diarywrite.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R
import com.hilingual.presentation.diarywrite.component.FeedbackCompleteContent
import com.hilingual.presentation.diarywrite.component.FeedbackMedia
import com.hilingual.presentation.diarywrite.component.FeedbackUIData

@Composable
internal fun DiarySuccessScreen(
    paddingValues: PaddingValues,
    diaryId: Long,
    onCloseButtonClick: () -> Unit,
    onShowFeedbackButtonClick: (diaryId: Long) -> Unit,
) {
    DiaryFeedbackStatusScreen(
        paddingValues = paddingValues,
        uiData = FeedbackUIData(
            title = "일기 저장 완료!",
            description = {
                Text(
                    text = "틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
                    color = HilingualTheme.colors.gray400,
                    style = HilingualTheme.typography.headR18,
                    textAlign = TextAlign.Center,
                )
            },
            media = FeedbackMedia.Lottie(
                resId = R.raw.lottie_feedback_complete,
                heightDp = 180.dp,
            ),
        ),
        content = {
            FeedbackCompleteContent(
                diaryId = diaryId,
                onCloseButtonClick = onCloseButtonClick,
                onShowFeedbackButtonClick = onShowFeedbackButtonClick,
            )
        },
    )
}

@Preview
@Composable
private fun DiarySuccessScreenPreview() {
    HilingualTheme {
        DiarySuccessScreen(
            paddingValues = PaddingValues(0.dp),
            diaryId = 0,
            onCloseButtonClick = {},
            onShowFeedbackButtonClick = {},
        )
    }
}
