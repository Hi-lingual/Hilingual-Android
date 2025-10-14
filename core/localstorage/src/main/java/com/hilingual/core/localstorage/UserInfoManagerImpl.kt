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
