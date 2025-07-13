package com.hilingual.core.localstorage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "hilingual_prefs")

@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenManager {

    @Volatile
    private var cachedAccessToken: String? = null

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveAccessToken(token: String) {
        cachedAccessToken = token
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN] = token
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        cachedAccessToken = accessToken
        context.dataStore.edit {
            it[PreferencesKeys.ACCESS_TOKEN] = accessToken
            it[PreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun getAccessToken(): String? {
        return cachedAccessToken ?: context.dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN].also {
            cachedAccessToken = it
        }
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN]
    }

    override suspend fun clearTokens() {
        cachedAccessToken = null
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
            preferences.remove(PreferencesKeys.REFRESH_TOKEN)
        }
    }
}
