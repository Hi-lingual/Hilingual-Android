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
import com.hilingual.core.localstorage.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TokenManagerImpl(
    private val dataStore: DataStore<UserPreferences>,
    private val externalScope: CoroutineScope
) : TokenManager {

    @Volatile
    private var cachedAccessToken: String? = null

    @Volatile
    private var cachedRefreshToken: String? = null

    init {
        externalScope.launch {
            val preferences = dataStore.data.first()
            cachedAccessToken = preferences.token
            cachedRefreshToken = preferences.refreshToken
        }
    }

    override suspend fun saveAccessToken(token: String) {
        cachedAccessToken = token
        dataStore.updateData { preferences ->
            preferences.copy(token = token)
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        cachedRefreshToken = token
        dataStore.updateData { preferences ->
            preferences.copy(refreshToken = token)
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        updateTokensInCache(accessToken, refreshToken)
        dataStore.updateData {
            it.copy(token = accessToken, refreshToken = refreshToken)
        }
    }

    override fun updateTokensInCache(accessToken: String, refreshToken: String) {
        cachedAccessToken = accessToken
        cachedRefreshToken = refreshToken
    }

    override fun getAccessToken(): String? = cachedAccessToken

    override fun getRefreshToken(): String? = cachedRefreshToken

    override suspend fun clearTokens() {
        cachedAccessToken = null
        cachedRefreshToken = null
        dataStore.updateData {
            it.copy(token = null, refreshToken = null)
        }
    }
}
