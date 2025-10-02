package com.hilingual.core.localstorage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.userInfoDataStore by preferencesDataStore(name = "hilingual_user_info_prefs")

@Singleton
class UserInfoManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserInfoManager {

    private object PreferencesKeys {
        val IS_REGISTER_COMPLETED = booleanPreferencesKey("is_register_completed")
        val IS_OTP_VERIFIED = booleanPreferencesKey("is_otp_verified")
    }

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        context.userInfoDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_REGISTER_COMPLETED] = isCompleted
        }
    }

    override suspend fun getRegisterStatus(): Boolean {
        return context.userInfoDataStore.data.first()[PreferencesKeys.IS_REGISTER_COMPLETED] ?: false
    }

    override suspend fun saveOtpVerified(isVerified: Boolean) {
        context.userInfoDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_OTP_VERIFIED] = isVerified
        }
    }

    override suspend fun isOtpVerified(): Boolean {
        return context.userInfoDataStore.data.first()[PreferencesKeys.IS_OTP_VERIFIED] ?: false
    }

    override suspend fun clear() {
        context.userInfoDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
