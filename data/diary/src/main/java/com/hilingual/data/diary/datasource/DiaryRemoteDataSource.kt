package com.hilingual.data.diary.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto

interface DiaryRemoteDataSource {
    suspend fun getDiaryContent(
        diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>

    suspend fun getDiaryFeedbacks(
        diaryId: Long
    ): BaseResponse<DiaryFeedbackResponseDto>

    suspend fun getDiaryRecommendExpressions(
        diaryId: Long
    ): BaseResponse<DiaryRecommendExpressionResponseDto>
}
