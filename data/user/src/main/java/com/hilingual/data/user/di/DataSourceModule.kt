package com.hilingual.data.user.di

import com.hilingual.data.user.datasouceimpl.UserRemoteDataSourceImpl
import com.hilingual.data.user.datasource.UserRemoteDataSource
import com.hilingual.data.user.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        userService: UserService
    ): UserRemoteDataSource = UserRemoteDataSourceImpl(userService)
}
