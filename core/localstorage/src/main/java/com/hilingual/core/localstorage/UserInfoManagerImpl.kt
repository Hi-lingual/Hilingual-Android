/*
 * Copyright 2025 The Hilingual Project
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
import kotlinx.coroutines.flow.first

class UserInfoManagerImpl constructor(
    private val dataStore: DataStore<Preferences>
) : UserInfoManager {

    private object PreferencesKeys {
        val IS_REGISTER_COMPLETED = booleanPreferencesKey("is_register_completed")
        val IS_OTP_VERIFIED = booleanPreferencesKey("is_otp_verified")
    }

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_REGISTER_COMPLETED] = isCompleted
        }
    }

    override suspend fun getRegisterStatus(): Boolean {
        return dataStore.data.first()[PreferencesKeys.IS_REGISTER_COMPLETED] ?: false
    }

    override suspend fun saveOtpVerified(isVerified: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_OTP_VERIFIED] = isVerified
        }
    }

    override suspend fun isOtpVerified(): Boolean {
        return dataStore.data.first()[PreferencesKeys.IS_OTP_VERIFIED] ?: false
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
