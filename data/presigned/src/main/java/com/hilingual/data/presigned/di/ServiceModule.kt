package com.hilingual.data.presigned.di

import com.hilingual.core.network.NoAuthClient
import com.hilingual.data.presigned.service.PresignedUrlService
import com.hilingual.data.presigned.service.S3Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providePresignedUrlService(retrofit: Retrofit): PresignedUrlService =
        retrofit.create(PresignedUrlService::class.java)

    @Provides
    @Singleton
    fun provideS3Service(@NoAuthClient retrofit: Retrofit): S3Service =
        retrofit.create(S3Service::class.java)
}
