package com.hilingual.data.onboarding.di

import com.hilingual.data.onboarding.repository.OnboardingRepository
import com.hilingual.data.onboarding.repositoryimpl.OnboardingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository
}
