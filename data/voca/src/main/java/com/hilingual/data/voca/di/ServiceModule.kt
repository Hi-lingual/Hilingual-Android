package com.hilingual.data.voca.di

import com.hilingual.data.voca.service.VocaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideVocaService(retrofit: Retrofit): VocaService =
        retrofit.create(VocaService::class.java)
}