package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto

interface FeedRemoteDataSource {
    suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto>

    suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto>
}
