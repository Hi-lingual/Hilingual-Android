package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

internal interface FeedService {
    @GET("/api/v1/feed/{diaryId}/users/profiles")
    suspend fun getFeedDiaryProfile(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<DiaryProfileResponseDto>
}