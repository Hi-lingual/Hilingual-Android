package com.hilingual.data.diary.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DiaryService {
    @GET("/api/v1/diaries/{diaryId}")
    suspend fun getDiaryContent(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>

    @GET("/api/v1/diaries/{diaryId}/feedbacks")
    suspend fun getDiaryFeedbacks(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryFeedbackResponseDto>

    @GET("/api/v1/diaries/{diaryId}/recommended")
    suspend fun getDiaryRecommendExpressions(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryRecommendExpressionResponseDto>
}
