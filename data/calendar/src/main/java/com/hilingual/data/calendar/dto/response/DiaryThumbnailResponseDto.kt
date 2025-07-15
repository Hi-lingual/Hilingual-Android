package com.hilingual.data.calendar.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryThumbnailResponseDto(
    @SerialName("diaryId")
    val diaryId: Long,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("originalText")
    val originalText: String
)
