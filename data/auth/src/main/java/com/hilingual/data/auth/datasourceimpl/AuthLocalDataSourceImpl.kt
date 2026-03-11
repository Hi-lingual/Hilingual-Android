package com.hilingual.data.auth.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hilingual.core.localstorage.constant.UserInfoDataStoreKey.KEY_IS_REGISTER_COMPLETED
import com.hilingual.core.localstorage.di.qualifier.UserInfoDataStore
import com.hilingual.data.auth.datasource.AuthLocalDataSource
import com.hilingual.data.auth.di.quilifier.TokenDataStore
import com.hilingual.data.auth.localstorage.model.TokenPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    @TokenDataStore private val tokenDataStore: DataStore<TokenPreferences>,
    @UserInfoDataStore private val userInfoDataStore: DataStore<Preferences>
) : AuthLocalDataSource {

    @Volatile
    private var cachedAccessToken: String? = null

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        cachedAccessToken = accessToken
        tokenDataStore.updateData {
            it.copy(accessToken = accessToken, refreshToken = refreshToken)
        }
    }

    override suspend fun getAccessToken(): String? {
        return cachedAccessToken ?: tokenDataStore.data.first().accessToken?.also {
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
