package com.hilingual.data.calendar.model

import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto

data class DiaryThumbnailModel(
    val diaryId: Long,
    val imageUrl: String?,
    val originalText: String
)

internal fun DiaryThumbnailResponseDto.toModel() = DiaryThumbnailModel(
    diaryId = this.diaryId,
    imageUrl = this.imageUrl,
    originalText = this.originalText
)
