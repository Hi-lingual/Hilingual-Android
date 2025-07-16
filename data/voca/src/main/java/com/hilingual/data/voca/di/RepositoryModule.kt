package com.hilingual.data.voca.di

import com.hilingual.data.voca.repository.VocaRepository
import com.hilingual.data.voca.repositoryimpl.VocaRepositoryImpl
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
    abstract fun bindsVocaRepository(
        vocaRepositoryImpl: VocaRepositoryImpl
    ): VocaRepository
}
