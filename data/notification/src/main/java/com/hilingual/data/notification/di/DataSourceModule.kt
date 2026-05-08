package com.hilingual.data.notification.di

import com.hilingual.data.notification.datasource.NotificationLocalDataSource
import com.hilingual.data.notification.datasourceimpl.NotificationLocalDataSourceImpl
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
    abstract fun bindsNotificationLocalDataSource(
        notificationLocalDataSourceImpl: NotificationLocalDataSourceImpl,
    ): NotificationLocalDataSource
}
