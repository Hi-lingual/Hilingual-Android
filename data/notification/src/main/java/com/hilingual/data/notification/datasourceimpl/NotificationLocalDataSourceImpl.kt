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
package com.hilingual.data.notification.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.hilingual.data.notification.datasource.NotificationLocalDataSource
import com.hilingual.data.notification.di.qualifier.NotificationDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class NotificationLocalDataSourceImpl @Inject constructor(
    @NotificationDataStore private val dataStore: DataStore<Preferences>,
) : NotificationLocalDataSource {
    private object PreferencesKeys {
        val IS_NOTIFICATION_DIALOG_SHOWN = booleanPreferencesKey("is_notification_dialog_shown")
    }

    override suspend fun getIsNotificationDialogShown(): Boolean =
        dataStore.data.first()[PreferencesKeys.IS_NOTIFICATION_DIALOG_SHOWN] ?: false

    override suspend fun updateIsNotificationDialogShown(isShown: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_NOTIFICATION_DIALOG_SHOWN] = isShown
        }
    }
}
