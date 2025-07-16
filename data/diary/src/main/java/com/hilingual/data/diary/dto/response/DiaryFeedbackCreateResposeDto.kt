package com.hilingual.data.diary.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryFeedbackCreateResposeDto(
    @SerialName("diaryId")
    val diaryId: Long
)