package com.hilingual.data.auth.di

import com.hilingual.core.network.service.TokenRefreshService
import com.hilingual.data.auth.service.TokenRefreshServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenRefreshServiceModule {

    @Binds
    @Singleton
    abstract fun bindTokenRefreshService(tokenRefreshServiceImpl: TokenRefreshServiceImpl): TokenRefreshService
}
