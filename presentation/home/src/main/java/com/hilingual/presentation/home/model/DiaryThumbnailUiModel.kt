package com.hilingual.presentation.home.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.calendar.model.DiaryThumbnailModel

@Immutable
data class DiaryThumbnailUiModel(
    val diaryId: Long,
    val imageUrl: String?,
    val originalText: String
)

fun DiaryThumbnailModel.toState() = DiaryThumbnailUiModel(
    diaryId = this.diaryId,
    imageUrl = this.imageUrl,
    originalText = this.originalText
)
