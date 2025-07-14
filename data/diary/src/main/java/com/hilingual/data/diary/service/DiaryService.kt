package com.hilingual.data.diary.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DiaryService {
    @GET("/api/v1/diaries/{diaryId}")
    suspend fun getDiaryContent(
        @Path(value = "diaryId") diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>
}
