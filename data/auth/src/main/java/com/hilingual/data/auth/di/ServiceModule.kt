package com.hilingual.data.auth.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.hilingual.core.network.RefreshClient
import com.hilingual.data.auth.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideAuthService(@RefreshClient retrofit: Retrofit): AuthService = 
        retrofit.create(AuthService::class.java)
}
