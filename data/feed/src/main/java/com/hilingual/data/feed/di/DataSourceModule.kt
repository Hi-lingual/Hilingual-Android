package com.hilingual.data.feed.di

import com.hilingual.data.feed.datasource.FeedRemoteDataSource
import com.hilingual.data.feed.datasourceimpl.FeedRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindFeedRemoteDataSource(impl: FeedRemoteDataSourceImpl): FeedRemoteDataSource
}
