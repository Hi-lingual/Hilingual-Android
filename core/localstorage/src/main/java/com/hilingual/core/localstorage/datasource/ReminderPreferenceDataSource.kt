package com.hilingual.core.localstorage.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hilingual.core.localstorage.constant.ReminderDataStoreKey
import com.hilingual.core.localstorage.di.qualifier.ReminderDataStore
import com.hilingual.core.localstorage.model.ReminderPreference
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderPreferenceDataSource @Inject constructor(
    @ReminderDataStore private val dataStore: DataStore<Preferences>,
) {
    val reminderFlow: Flow<ReminderPreference> = dataStore.data.map { prefs ->
        ReminderPreference(
            isEnabled = prefs[ReminderDataStoreKey.IS_ENABLED] ?: false,
            hour = prefs[ReminderDataStoreKey.HOUR] ?: 0,
            minute = prefs[ReminderDataStoreKey.MINUTE] ?: 0,
            isDailyRepeat = prefs[ReminderDataStoreKey.IS_DAILY_REPEAT] ?: true,
            selectedDays = prefs[ReminderDataStoreKey.SELECTED_DAYS] ?: emptySet(),
        )
    }

    suspend fun save(preference: ReminderPreference) {
        dataStore.edit { prefs ->
            prefs[ReminderDataStoreKey.IS_ENABLED] = preference.isEnabled
            prefs[ReminderDataStoreKey.HOUR] = preference.hour
            prefs[ReminderDataStoreKey.MINUTE] = preference.minute
            prefs[ReminderDataStoreKey.IS_DAILY_REPEAT] = preference.isDailyRepeat
            prefs[ReminderDataStoreKey.SELECTED_DAYS] = preference.selectedDays
        }
    }

    suspend fun setEnabled(isEnabled: Boolean) {
        dataStore.edit { prefs -> prefs[ReminderDataStoreKey.IS_ENABLED] = isEnabled }
    }
}
