package com.hilingual.presentation.widget.di

import com.hilingual.core.common.widget.WidgetUpdater
import com.hilingual.presentation.widget.common.WidgetUpdaterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WidgetModule {
    @Binds
    @Singleton
    abstract fun bindsWidgetUpdater(impl: WidgetUpdaterImpl): WidgetUpdater
}
