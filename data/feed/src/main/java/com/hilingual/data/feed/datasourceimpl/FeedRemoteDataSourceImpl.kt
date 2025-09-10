package com.hilingual.data.feed.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.UserResponseDto
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

    override suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto> =
        feedService.getRecommendFeeds()

    override suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto> =
        feedService.getFollowingFeeds()

    override suspend fun getFeedDiaryProfile(diaryId: Long): BaseResponse<DiaryProfileResponseDto> =
        feedService.getFeedDiaryProfile(diaryId)

    override suspend fun postIsLike(diaryId: Long, likeRequestDto: LikeRequestDto): BaseResponse<Unit> =
        feedService.postIsLike(diaryId, likeRequestDto)

    override suspend fun getUserSearchResult(keyword: String): BaseResponse<UserResponseDto> =
        feedService.getUserSearchResult(keyword)
}
