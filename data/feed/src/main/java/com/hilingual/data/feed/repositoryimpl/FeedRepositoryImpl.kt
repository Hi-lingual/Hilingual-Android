package com.hilingual.data.feed.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.model.FeedDiaryProfileModel
import com.hilingual.data.feed.model.FeedProfileModel
import com.hilingual.data.feed.model.LikedDiariesModel
import com.hilingual.data.feed.model.SharedDiariesModel
import com.hilingual.data.feed.model.FeedListModel
import com.hilingual.data.feed.model.toModel
import com.hilingual.data.feed.repository.FeedRepository
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val feedRemoteDataSource: FeedRemoteDataSource
) : FeedRepository {
    override suspend fun getFeedProfile(targetUserId: Long): Result<FeedProfileModel> =
        suspendRunCatching {
            feedRemoteDataSource.getFeedProfile(targetUserId = targetUserId).data!!.toModel()
        }

    override suspend fun getSharedDiaries(targetUserId: Long): Result<SharedDiariesModel> =
        suspendRunCatching {
            feedRemoteDataSource.getSharedDiaries(targetUserId = targetUserId).data!!.toModel()
        }

    override suspend fun getLikedDiaries(targetUserId: Long): Result<LikedDiariesModel> =
        suspendRunCatching {
            feedRemoteDataSource.getLikedDiaries(targetUserId = targetUserId).data!!.toModel()
        }

    override suspend fun getRecommendFeeds(): Result<FeedListModel> =
        suspendRunCatching {
            feedRemoteDataSource.getRecommendFeeds().data!!.toModel()
        }

    override suspend fun getFollowingFeeds(): Result<FeedListModel> =
        suspendRunCatching {
            feedRemoteDataSource.getFollowingFeeds().data!!.toModel()
        }

    override suspend fun getFeedDiaryProfile(diaryId: Long): Result<FeedDiaryProfileModel> =
        suspendRunCatching {
            feedRemoteDataSource.getFeedDiaryProfile(diaryId).data!!.toModel()
        }

    override suspend fun postIsLike(
        diaryId: Long,
        isLiked: Boolean
    ): Result<Unit> = suspendRunCatching {
        feedRemoteDataSource.postIsLike(
            diaryId = diaryId,
            likeRequestDto = LikeRequestDto(isLiked)
        )
    }
}
