package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import retrofit2.http.GET

internal interface FeedService {
    @GET("/api/v1/feed/recommend")
    suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto>

    @GET("/api/v1/feed/following")
    suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto>
}
