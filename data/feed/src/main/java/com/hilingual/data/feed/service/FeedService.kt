package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/api/v1/feed/recommend")
    suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto>

    @GET("/api/v1/feed/following")
    suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto>

    @GET("/api/v1/feed/{diaryId}/users/profiles")
    suspend fun getFeedDiaryProfile(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<DiaryProfileResponseDto>

    @POST("/api/v1/feed/likes/{diaryId}")
    suspend fun postIsLike(
        @Path("diaryId") diaryId: Long,
        @Body likeRequestDto: LikeRequestDto
    ): BaseResponse<Unit>

    @GET("/api/v1/feed/search")
    suspend fun getUserSearchResult(
        @Query("keyword") keyword: String
    ): BaseResponse<UserResponseDto>
}
