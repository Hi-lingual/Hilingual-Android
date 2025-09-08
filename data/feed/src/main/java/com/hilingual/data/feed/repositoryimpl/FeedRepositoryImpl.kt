package com.hilingual.data.feed.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.model.FeedDiaryProfileModel
import com.hilingual.data.feed.model.toModel
import com.hilingual.data.feed.repository.FeedRepository
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val feedRemoteDataSource: FeedRemoteDataSource
) : FeedRepository {
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
