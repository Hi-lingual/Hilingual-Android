package com.hilingual.core.work.di

import com.hilingual.core.work.repository.WorkRepository
import com.hilingual.core.work.repository.WorkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WorkModule {

    @Binds
    @Singleton
    abstract fun bindWorkRepository(
        workRepositoryImpl: WorkRepositoryImpl,
    ): WorkRepository
}
