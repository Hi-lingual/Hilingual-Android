package com.hilingual.di

import com.hilingual.core.common.provider.DeviceInfoProvider
import com.hilingual.app.DeviceInfoProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProviderModule {

    @Binds
    @Singleton
    abstract fun bindDeviceInfoProvider(
        impl: DeviceInfoProviderImpl
    ): DeviceInfoProvider
}
