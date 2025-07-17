package com.hilingual.data.diary.di

import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.datasourceimpl.DiaryRemoteDataSourceImpl
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
    abstract fun bindDiaryRemoteDataSource(impl: DiaryRemoteDataSourceImpl): DiaryRemoteDataSource
}
