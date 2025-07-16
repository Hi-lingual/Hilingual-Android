package com.hilingual.presentation.diarywrite.component

import androidx.annotation.RawRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.presentation.diarywrite.R

sealed class DiaryFeedbackState() {
    object Default : DiaryFeedbackState()
    object Loading : DiaryFeedbackState()
    data class Complete(val diaryId: Long) : DiaryFeedbackState()
    data class Failure(val throwable: Throwable) : DiaryFeedbackState()
}

data class FeedbackUIData(
    val title: String = "",
    val description: String = "",
    @RawRes val lottieRawRes: Int = R.raw.lottie_feedback_loading,
    val lottieRawResHeightDp: Dp = 0.dp
)
