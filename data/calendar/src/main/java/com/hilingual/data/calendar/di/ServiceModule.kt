package com.hilingual.data.calendar.di

import com.hilingual.data.calendar.service.CalendarService
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
    fun provideCalendarService(retrofit: Retrofit): CalendarService = retrofit.create(CalendarService::class.java)
}
