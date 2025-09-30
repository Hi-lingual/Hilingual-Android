package com.hilingual.di

import com.hilingual.analytics.AmplitudeTracker
import com.hilingual.core.common.analytics.Tracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackerModule {

    @Binds
    abstract fun bindTracker(
        amplitudeTracker: AmplitudeTracker
    ): Tracker
}
