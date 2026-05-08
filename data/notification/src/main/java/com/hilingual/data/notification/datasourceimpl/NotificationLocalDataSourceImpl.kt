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
