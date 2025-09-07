package com.hilingual.data.presigned.di

import com.hilingual.data.presigned.service.PresignedUrlService
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
}
