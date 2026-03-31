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
package com.hilingual.data.user.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hilingual.core.localstorage.constant.UserInfoDataStoreKey.KEY_IS_REGISTER_COMPLETED
import com.hilingual.core.localstorage.di.qualifier.UserInfoDataStore
import com.hilingual.data.user.datasource.UserLocalDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserLocalDataSourceImpl @Inject constructor(
    @UserInfoDataStore private val dataStore: DataStore<Preferences>,
) : UserLocalDataSource {

    override suspend fun saveRegisterStatus(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_REGISTER_COMPLETED] = isCompleted
        }
    }

    override suspend fun getRegisterStatus(): Boolean = dataStore.data.map { preferences ->
        preferences[KEY_IS_REGISTER_COMPLETED] ?: false
    }.first()

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
