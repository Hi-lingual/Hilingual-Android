package com.hilingual.data.feed.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
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
}
