package com.hilingual.data.widget.di

import com.hilingual.data.widget.repository.WidgetRepository
import com.hilingual.data.widget.repositoryimpl.WidgetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsWidgetRepository(
        widgetRepositoryImpl: WidgetRepositoryImpl,
    ): WidgetRepository
}
