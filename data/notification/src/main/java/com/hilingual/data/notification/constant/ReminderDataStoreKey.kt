package com.hilingual.data.notification.constant

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal object ReminderDataStoreKey {
    val IS_ENABLED = booleanPreferencesKey("reminder_is_enabled")
    val HOUR = intPreferencesKey("reminder_hour")
    val MINUTE = intPreferencesKey("reminder_minute")
    val SELECTED_DAYS = stringSetPreferencesKey("reminder_selected_days")
}
