package com.hilingual.data.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.hilingual.data.auth.datasource.AuthRemoteDataSource
import com.hilingual.data.auth.datasource.GoogleAuthDataSource
import com.hilingual.data.auth.datasourceimpl.AuthRemoteDataSourceImpl
import com.hilingual.data.auth.datasourceimpl.GoogleAuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    abstract fun bindAuthApiDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
            return CredentialManager.create(context)
        }
    }
}
