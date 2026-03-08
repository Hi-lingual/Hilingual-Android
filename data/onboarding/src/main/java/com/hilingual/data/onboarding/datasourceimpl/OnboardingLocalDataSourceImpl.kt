package com.hilingual.data.onboarding.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.hilingual.data.onboarding.datasource.OnboardingLocalDataSource
import com.hilingual.data.onboarding.di.OnboardingDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class OnboardingLocalDataSourceImpl @Inject constructor(
    @OnboardingDataStore private val dataStore: DataStore<Preferences>
) : OnboardingLocalDataSource {
    private object PreferencesKeys {
        val IS_HOME_ONBOARDING_COMPLETED = booleanPreferencesKey("is_home_onboarding_completed")
        val IS_SPLASH_ONBOARDING_COMPLETED = booleanPreferencesKey("is_splash_onboarding_completed")
    }

    override suspend fun getIsHomeOnboardingCompleted(): Boolean =
        dataStore.data.first()[PreferencesKeys.IS_HOME_ONBOARDING_COMPLETED] ?: true

    override suspend fun updateIsHomeOnboardingCompleted(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_HOME_ONBOARDING_COMPLETED] = isCompleted
        }
    }

    override suspend fun getIsSplashOnboardingCompleted(): Boolean =
        dataStore.data.first()[PreferencesKeys.IS_SPLASH_ONBOARDING_COMPLETED] ?: false

    override suspend fun updateIsSplashOnboardingCompleted(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SPLASH_ONBOARDING_COMPLETED] = isCompleted
        }
    }
}
