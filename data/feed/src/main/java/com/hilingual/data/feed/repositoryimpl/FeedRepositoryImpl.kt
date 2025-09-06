package com.hilingual.data.feed.repositoryimpl

import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.repository.FeedRepository
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val feedRemoteDataSource: FeedRemoteDataSource
) : FeedRepository
