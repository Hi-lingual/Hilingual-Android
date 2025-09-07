package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface FeedService {
    @GET("/api/v1/feed/{diaryId}/users/profiles")
    suspend fun getFeedDiaryProfile(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<DiaryProfileResponseDto>

    @POST("/api/v1/feed/likes/{diaryId}")
    suspend fun postIsLike(
        @Path("diaryId") diaryId: Long,
        @Body likeRequestDto: LikeRequestDto
    ): BaseResponse<Unit>
}
