package com.hilingual.data.notification.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hilingual.data.notification.constant.ReminderDataStoreKey
import com.hilingual.data.notification.datasource.ReminderLocalDataSource
import com.hilingual.data.notification.di.qualifier.NotificationDataStore
import com.hilingual.data.notification.model.ReminderPreference
import java.time.DayOfWeek
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class ReminderLocalDataSourceImpl @Inject constructor(
    @NotificationDataStore private val dataStore: DataStore<Preferences>,
) : ReminderLocalDataSource {

    override suspend fun getReminder(): ReminderPreference {
        val prefs = dataStore.data.first()
        val default = ReminderPreference()
        return ReminderPreference(
            isEnabled = prefs[ReminderDataStoreKey.IS_ENABLED] ?: default.isEnabled,
            hour = prefs[ReminderDataStoreKey.HOUR] ?: default.hour,
            minute = prefs[ReminderDataStoreKey.MINUTE] ?: default.minute,
            isDailyRepeat = prefs[ReminderDataStoreKey.IS_DAILY_REPEAT] ?: default.isDailyRepeat,
            selectedDays = prefs[ReminderDataStoreKey.SELECTED_DAYS]?.toDayOfWeekSet() ?: default.selectedDays,
        )
    }

    override suspend fun saveReminder(preference: ReminderPreference) {
        dataStore.edit { prefs ->
            prefs[ReminderDataStoreKey.IS_ENABLED] = preference.isEnabled
            prefs[ReminderDataStoreKey.HOUR] = preference.hour
            prefs[ReminderDataStoreKey.MINUTE] = preference.minute
            prefs[ReminderDataStoreKey.IS_DAILY_REPEAT] = preference.isDailyRepeat
            prefs[ReminderDataStoreKey.SELECTED_DAYS] = preference.selectedDays.map { it.name }.toSet()
        }
    }

    override suspend fun updateReminderEnabled(isEnabled: Boolean) {
        dataStore.edit { prefs -> prefs[ReminderDataStoreKey.IS_ENABLED] = isEnabled }
    }

    override suspend fun clearReminder() {
        dataStore.edit { prefs ->
            prefs.remove(ReminderDataStoreKey.IS_ENABLED)
            prefs.remove(ReminderDataStoreKey.HOUR)
            prefs.remove(ReminderDataStoreKey.MINUTE)
            prefs.remove(ReminderDataStoreKey.IS_DAILY_REPEAT)
            prefs.remove(ReminderDataStoreKey.SELECTED_DAYS)
        }
    }
}

private fun Set<String>.toDayOfWeekSet(): Set<DayOfWeek> =
    mapNotNull { name -> runCatching { DayOfWeek.valueOf(name) }.getOrNull() }.toSet()
