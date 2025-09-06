package com.hilingual.data.feed.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.model.FeedProfileModel
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
}
