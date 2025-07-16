package com.hilingual.data.voca.di

import com.hilingual.data.voca.service.VocaService
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
    fun provideVocaService(retrofit: Retrofit): VocaService =
        retrofit.create(VocaService::class.java)
}
