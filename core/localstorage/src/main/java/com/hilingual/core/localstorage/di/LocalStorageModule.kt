package com.hilingual.core.localstorage.di

import com.hilingual.core.localstorage.TokenManager
import com.hilingual.core.localstorage.TokenManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalStorageModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(tokenManagerImpl: TokenManagerImpl): TokenManager
}
