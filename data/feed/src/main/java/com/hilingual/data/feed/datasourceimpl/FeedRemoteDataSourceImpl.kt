package com.hilingual.data.feed.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import com.hilingual.data.feed.service.FeedService
import javax.inject.Inject

internal class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedService: FeedService
) : FeedRemoteDataSource {
    override suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto> =
        feedService.getRecommendFeeds()

    override suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto> =
        feedService.getFollowingFeeds()

    override suspend fun getFeedDiaryProfile(diaryId: Long): BaseResponse<DiaryProfileResponseDto> =
        feedService.getFeedDiaryProfile(diaryId)

    override suspend fun postIsLike(diaryId: Long, likeRequestDto: LikeRequestDto): BaseResponse<Unit> =
        feedService.postIsLike(diaryId, likeRequestDto)
}
