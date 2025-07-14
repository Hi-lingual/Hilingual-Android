package com.hilingual.data.user.di

import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.data.user.repositoryimpl.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository = UserRepositoryImpl(userRemoteDataSource)
}
