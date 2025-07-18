package com.hilingual.presentation.diarywrite.component

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.ui.unit.Dp

sealed class DiaryFeedbackState() {
    object Default : DiaryFeedbackState()
    object Loading : DiaryFeedbackState()
    data class Complete(val diaryId: Long) : DiaryFeedbackState()
    data class Failure(val throwable: Throwable) : DiaryFeedbackState()
}

sealed class FeedbackMedia {
    data class Lottie(
        @RawRes val resId: Int,
        val heightDp: Dp
    ) : FeedbackMedia()

    data class Image(
        @DrawableRes val resId: Int,
        val heightDp: Dp
    ) : FeedbackMedia()
}

data class FeedbackUIData(
    val title: String = "",
    val description: String? = null,
    val media: FeedbackMedia
)
