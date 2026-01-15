package com.hilingual.data.config.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.hilingual.data.config.constant.DEFAULT_VERSION
import com.hilingual.data.config.constant.KEY_LATEST_VERSION
import com.hilingual.data.config.constant.KEY_MIN_FORCE_VERSION
import com.hilingual.data.config.constant.MIN_FETCH_INTERVAL_SECONDS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(
            mapOf(
                KEY_MIN_FORCE_VERSION to DEFAULT_VERSION,
                KEY_LATEST_VERSION to DEFAULT_VERSION
            )
        )

        return remoteConfig
    }
}
