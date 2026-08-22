package com.hilingual.data.widget.di

import com.hilingual.data.widget.service.WidgetService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideWidgetService(retrofit: Retrofit): WidgetService = retrofit.create(WidgetService::class.java)
}
