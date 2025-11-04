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
import kotlinx.coroutines.flow.first

class TokenManagerImpl constructor(
    private val dataStore: DataStore<UserPreferences>
) : TokenManager {

    @Volatile
    private var cachedAccessToken: String? = null

    override suspend fun saveAccessToken(token: String) {
        cachedAccessToken = token
        dataStore.updateData { preferences ->
            preferences.copy(token = token)
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        dataStore.updateData { preferences ->
            preferences.copy(refreshToken = token)
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        cachedAccessToken = accessToken
        dataStore.updateData {
            it.copy(token = accessToken, refreshToken = refreshToken)
        }
    }

    override suspend fun getAccessToken(): String? {
        return cachedAccessToken ?: dataStore.data.first().token?.also {
            cachedAccessToken = it
        }
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first().refreshToken
    }

    override suspend fun clearTokens() {
        cachedAccessToken = null
        dataStore.updateData {
            it.copy(token = null, refreshToken = null)
        }
    }
}
