package com.hilingual.data.widget.di

import com.hilingual.data.widget.datasource.WidgetRemoteDataSource
import com.hilingual.data.widget.datasourceimpl.WidgetRemoteDataSourceImpl
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
    abstract fun bindsWidgetRemoteDataSource(
        widgetRemoteDataSourceImpl: WidgetRemoteDataSourceImpl,
    ): WidgetRemoteDataSource
}
