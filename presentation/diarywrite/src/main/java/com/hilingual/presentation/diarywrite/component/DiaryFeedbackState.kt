package com.hilingual.presentation.diarywrite.component

import androidx.annotation.RawRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.presentation.diarywrite.R

internal enum class DiaryFeedbackState(
    val data: FeedbackUIData? = null
) {
    Default,
    Loading(
        FeedbackUIData(
            title = "일기 저장 중..",
            description = "피드백을 요청하고 있어요.",
            lottieRawRes = R.raw.lottie_feedback_loading,
            lottieRawResHeightDp = 194.dp
        )
    ),
    Complete(
        FeedbackUIData(
            title = "AI 피드백 완료!",
            description = "틀린 부분을 고치고,\n더 나은 표현으로 수정했어요!",
            lottieRawRes = R.raw.lottie_feedback_complete,
            lottieRawResHeightDp = 180.dp
        )
    )
}

data class FeedbackUIData(
    val title: String = "",
    val description: String = "",
    @RawRes val lottieRawRes: Int = R.raw.lottie_feedback_loading,
    val lottieRawResHeightDp: Dp = 0.dp
)
