/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.core.localstorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class OnboardingStateManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnboardingStateManager {
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
