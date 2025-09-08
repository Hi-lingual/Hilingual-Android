package com.hilingual.data.presigned.di

import com.hilingual.data.presigned.datasource.FileUploaderRemoteDataSource
import com.hilingual.data.presigned.datasource.PresignedUrlRemoteDataSource
import com.hilingual.data.presigned.datasourceimpl.FileUploaderRemoteDataSourceImpl
import com.hilingual.data.presigned.datasourceimpl.PresignedUrlRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindPresignedUrlRemoteDataSource(
        presignedUrlRemoteDataSourceImpl: PresignedUrlRemoteDataSourceImpl
    ): PresignedUrlRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFileUploaderRemoteDataSource(
        fileUploaderRemoteDataSourceImpl: FileUploaderRemoteDataSourceImpl
    ): FileUploaderRemoteDataSource
}
