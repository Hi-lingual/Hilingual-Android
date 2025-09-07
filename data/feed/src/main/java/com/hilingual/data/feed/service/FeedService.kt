package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.request.IsLikedRequestDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FeedService {
    @GET("/api/v1/feed/profiles/{targetUserId}")
    suspend fun getFeedProfile(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<FeedProfileResponseDto>

    @GET("/api/v1/feed/profiles/{targetUserId}/diaries/shared")
    suspend fun getSharedDiaries(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<SharedDiariesResponseDto>

    @GET("/api/v1/feed/profiles/{targetUserId}/diaries/liked")
    suspend fun getLikedDiaries(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<LikedDiariesResponseDto>

    @POST("/api/v1/feed/likes/{diaryId}")
    suspend fun postIsLiked(
        @Path("diaryId") diaryId: Long,
        @Body isLikedRequestDto: IsLikedRequestDto
    ): BaseResponse<Unit>
}
