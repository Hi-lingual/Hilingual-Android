package com.hilingual.data.presigned.di

import com.hilingual.data.presigned.repository.FileUploaderRepository
import com.hilingual.data.presigned.repository.PresignedUrlRepository
import com.hilingual.data.presigned.repositoryimpl.FileUploaderRepositoryImpl
import com.hilingual.data.presigned.repositoryimpl.PresignedUrlRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPresignedUrlRepository(
        presignedUrlRepositoryImpl: PresignedUrlRepositoryImpl
    ): PresignedUrlRepository

    @Binds
    @Singleton
    abstract fun bindFileUploaderRepository(
        fileUploaderRepositoryImpl: FileUploaderRepositoryImpl
    ): FileUploaderRepository
}
