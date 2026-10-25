package com.hilingual.data.notification.repository

import com.hilingual.data.notification.model.ReminderPreference

interface ReminderRepository {
    fun isExactAlarmAllowed(): Boolean
    suspend fun getReminder(): Result<ReminderPreference>
    suspend fun saveReminder(preference: ReminderPreference): Result<Unit>
    suspend fun disableReminder(): Result<Unit>
    suspend fun rescheduleIfEnabled(): Result<Unit>
    suspend fun clearReminder(): Result<Unit>
}
