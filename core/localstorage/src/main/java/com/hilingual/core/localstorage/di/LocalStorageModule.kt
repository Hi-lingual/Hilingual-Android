package com.hilingual.core.localstorage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.core.localstorage.constant.DataStoreConstant
import com.hilingual.core.localstorage.di.qualifier.DiaryTempDataStore
import com.hilingual.core.localstorage.di.qualifier.OnboardingDataStore
import com.hilingual.core.localstorage.di.qualifier.UserInfoDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    @UserInfoDataStore
    fun provideUserInfoDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = DataStoreConstant.HILINGUAL_USER_INFO_PREFS
    )

    @Provides
    @Singleton
    @DiaryTempDataStore
    fun provideDiaryTempDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = DataStoreConstant.HILINGUAL_DIARY_TEMP_PREFS
    )

    @Provides
    @Singleton
    @OnboardingDataStore
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = DataStoreConstant.HILINGUAL_ONBOARDING_STATE_PREFS
    )
}
