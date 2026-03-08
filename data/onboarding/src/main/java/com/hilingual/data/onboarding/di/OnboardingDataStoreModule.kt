package com.hilingual.data.onboarding.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hilingual.core.localstorage.builder.DataStoreBuilder
import com.hilingual.data.onboarding.di.qualifier.OnboardingDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingDataStoreModule {

    private const val HILINGUAL_ONBOARDING_STATE_PREFS = "hilingual_onboarding_state_prefs"

    @Provides
    @Singleton
    @OnboardingDataStore
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = DataStoreBuilder.createPreferencesDataStore(
        context = context,
        name = HILINGUAL_ONBOARDING_STATE_PREFS
    )
}
