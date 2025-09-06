package com.hilingual.data.feed.datasourceimpl

import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.service.FeedService
import javax.inject.Inject

internal class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedService: FeedService
) : FeedRemoteDataSource
