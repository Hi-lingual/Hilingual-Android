package com.hilingual.data.diary.di

import com.hilingual.core.network.MultipartClient
import com.hilingual.data.diary.service.DiaryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Provides
    @Singleton
    fun provideDiaryService(@MultipartClient retrofit: Retrofit): DiaryService = retrofit.create(DiaryService::class.java)
}
