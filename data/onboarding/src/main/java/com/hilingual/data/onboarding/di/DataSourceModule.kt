package com.hilingual.data.onboarding.di

import com.hilingual.data.onboarding.localstorage.OnboardingLocalDataSource
import com.hilingual.data.onboarding.localstorage.OnboardingLocalDataSourceImpl
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
    abstract fun bindsOnboardingLocalDataSource(
        onboardingLocalDataSourceImpl: OnboardingLocalDataSourceImpl
    ): OnboardingLocalDataSource
}
