package com.hilingual.data.notification.repository

import com.hilingual.data.notification.model.ReminderPreference

interface ReminderRepository {
    suspend fun getReminder(): ReminderPreference
    suspend fun saveReminder(preference: ReminderPreference)
    suspend fun disableReminder()
    suspend fun rescheduleIfEnabled()
}
