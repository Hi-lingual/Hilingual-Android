package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FeedService {
    @GET("/api/v1/feed/profiles/{targetUserId}")
    suspend fun getFeedProfile(
        @Path("targetUserId") targetUserId: Long
    ) : BaseResponse<FeedProfileResponseDto>
}