package com.hilingual.data.auth.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.hilingual.data.auth.di.quilifier.AuthDataStore
import com.hilingual.data.auth.datasource.AuthLocalDataSource
import com.hilingual.data.auth.localstorage.model.TokenPreferences
import com.hilingual.core.localstorage.constant.UserInfoDataStoreKey.KEY_IS_REGISTER_COMPLETED
import com.hilingual.core.localstorage.di.qualifier.UserInfoDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    @AuthDataStore private val tokenDataStore: DataStore<TokenPreferences>,
    @UserInfoDataStore private val userInfoDataStore: DataStore<Preferences>
) : AuthLocalDataSource {

    @Volatile
    private var cachedAccessToken: String? = null

    override suspend fun saveAccessToken(token: String) {
        cachedAccessToken = token
        tokenDataStore.updateData { preferences ->
            preferences.copy(token = token)
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        tokenDataStore.updateData { preferences ->
            preferences.copy(refreshToken = token)
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        cachedAccessToken = accessToken
        tokenDataStore.updateData {
            it.copy(token = accessToken, refreshToken = refreshToken)
        }
    }

    override suspend fun getAccessToken(): String? {
        return cachedAccessToken ?: tokenDataStore.data.first().token?.also {
            cachedAccessToken = it
        }
    }

    override suspend fun getRefreshToken(): String? {
        return tokenDataStore.data.first().refreshToken
    }

    override suspend fun clearTokens() {
        cachedAccessToken = null
        tokenDataStore.updateData {
            it.copy(token = null, refreshToken = null)
        }
    }

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        userInfoDataStore.edit { preferences ->
            preferences[KEY_IS_REGISTER_COMPLETED] = isCompleted
        }
    }

    override suspend fun clearUserInfo() {
        userInfoDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
