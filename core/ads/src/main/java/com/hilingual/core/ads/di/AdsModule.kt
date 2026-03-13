package com.hilingual.core.ads.di

import com.hilingual.core.ads.initializer.AdsInitializer
import com.hilingual.core.ads.initializer.GmaAdsInitializer
import com.hilingual.core.ads.manager.AdsPreloadManager
import com.hilingual.core.ads.manager.AdsPreloadManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AdsModule {

    @Binds
    @Singleton
    abstract fun bindAdsInitializer(
        gmaAdsInitializer: GmaAdsInitializer,
    ): AdsInitializer

    @Binds
    @Singleton
    abstract fun bindAdsPreloadManager(
        adsPreloadManagerImpl: AdsPreloadManagerImpl,
    ): AdsPreloadManager
}
