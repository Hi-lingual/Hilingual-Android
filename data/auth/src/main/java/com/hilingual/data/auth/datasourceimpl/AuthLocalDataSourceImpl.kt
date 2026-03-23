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
package com.hilingual.data.auth.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hilingual.core.localstorage.constant.UserInfoDataStoreKey.KEY_IS_REGISTER_COMPLETED
import com.hilingual.core.localstorage.di.qualifier.UserInfoDataStore
import com.hilingual.data.auth.datasource.AuthLocalDataSource
import com.hilingual.data.auth.di.qualifier.TokenDataStore
import com.hilingual.data.auth.localstorage.model.TokenPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class AuthLocalDataSourceImpl @Inject constructor(
    @TokenDataStore private val tokenDataStore: DataStore<TokenPreferences>,
    @UserInfoDataStore private val userInfoDataStore: DataStore<Preferences>,
) : AuthLocalDataSource {

    @Volatile
    private var cachedAccessToken: String? = null

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        cachedAccessToken = accessToken
        tokenDataStore.updateData {
            it.copy(accessToken = accessToken, refreshToken = refreshToken)
        }
    }

    override suspend fun getAccessToken(): String? =
        cachedAccessToken ?: tokenDataStore.data.first().accessToken?.also {
            cachedAccessToken = it
        }

    override suspend fun getRefreshToken(): String? = tokenDataStore.data.first().refreshToken

    override suspend fun clearTokens() {
        cachedAccessToken = null
        tokenDataStore.updateData {
            it.copy(accessToken = null, refreshToken = null)
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
