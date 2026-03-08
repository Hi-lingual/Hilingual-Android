package com.hilingual.core.localstorage.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {
    // DataStore builders and providers are moved to data modules.
    // Use DataStoreBuilder for creation.
}
