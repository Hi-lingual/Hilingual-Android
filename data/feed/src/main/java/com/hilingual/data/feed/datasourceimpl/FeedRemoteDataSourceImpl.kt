package com.hilingual.data.feed.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.dto.request.IsLikedRequestDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.service.FeedService
import javax.inject.Inject

internal class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedService: FeedService
) : FeedRemoteDataSource {
    override suspend fun getFeedProfile(targetUserId: Long): BaseResponse<FeedProfileResponseDto> =
        feedService.getFeedProfile(targetUserId = targetUserId)
    override suspend fun getSharedDiaries(targetUserId: Long): BaseResponse<SharedDiariesResponseDto> =
        feedService.getSharedDiaries(targetUserId = targetUserId)
    override suspend fun getLikedDiaries(targetUserId: Long): BaseResponse<LikedDiariesResponseDto> =
        feedService.getLikedDiaries(targetUserId = targetUserId)
    override suspend fun postIsLiked(diaryId: Long, isLikedRequestDto: IsLikedRequestDto): BaseResponse<Unit> =
        feedService.postIsLiked(diaryId = diaryId, isLikedRequestDto = isLikedRequestDto)
}
