package com.hilingual.data.calendar.di

import com.hilingual.data.calendar.datasouceimpl.CalendarRemoteDataSourceImpl
import com.hilingual.data.calendar.datasource.CalendarRemoteDataSource
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
    abstract fun bindsCalendarRemoteDataSource(
        calendarRemoteDataSourceImpl: CalendarRemoteDataSourceImpl
    ): CalendarRemoteDataSource
}
