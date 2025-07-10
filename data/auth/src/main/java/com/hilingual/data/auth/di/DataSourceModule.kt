package com.hilingual.data.auth.di

import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.datasourceimpl.AuthRemoteDataSourceImpl
import com.hilingual.data.auth.datasourceimpl.GoogleAuthDataSourceImpl
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
    abstract fun bindGoogleAuthDataSource(
        googleAuthDataSourceImpl: GoogleAuthDataSourceImpl
    ): GoogleAuthDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource
}
