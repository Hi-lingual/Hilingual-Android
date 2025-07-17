package com.hilingual.data.diary.model

import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResponseDto

data class DiaryFeedbackCreateModel(
    val diaryId: Long
)

internal fun DiaryFeedbackCreateResponseDto.toModel() = DiaryFeedbackCreateModel(
    diaryId = this.diaryId
)
