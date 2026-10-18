package com.hilingual.data.notification.datasource

import com.hilingual.data.notification.model.ReminderPreference

interface ReminderLocalDataSource {
    suspend fun getReminder(): ReminderPreference
    suspend fun saveReminder(preference: ReminderPreference)
    suspend fun updateReminderEnabled(isEnabled: Boolean)
}
