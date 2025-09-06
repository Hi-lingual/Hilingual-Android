package com.hilingual.data.feed.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.service.FeedService
import javax.inject.Inject

internal class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedService: FeedService
) : FeedRemoteDataSource {
    override suspend fun getFeedProfile(targetUserId: Long): BaseResponse<FeedProfileResponseDto> =
        feedService.getFeedProfile(targetUserId = targetUserId)
}
