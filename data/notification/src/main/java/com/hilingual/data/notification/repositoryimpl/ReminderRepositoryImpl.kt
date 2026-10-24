package com.hilingual.data.notification.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.notification.datasource.ReminderLocalDataSource
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.reminder.ReminderScheduler
import com.hilingual.data.notification.repository.ReminderRepository
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val localDataSource: ReminderLocalDataSource,
    private val reminderScheduler: ReminderScheduler,
) : ReminderRepository {

    override suspend fun getReminder(): Result<ReminderPreference> =
        suspendRunCatching { localDataSource.getReminder() }

    override suspend fun saveReminder(preference: ReminderPreference): Result<Unit> =
        suspendRunCatching {
            localDataSource.saveReminder(preference)
            reminderScheduler.schedule(preference)
        }

    override suspend fun disableReminder(): Result<Unit> =
        suspendRunCatching {
            localDataSource.updateReminderEnabled(false)
            reminderScheduler.cancel()
        }

    override suspend fun rescheduleIfEnabled(): Result<Unit> =
        suspendRunCatching {
            val preference = localDataSource.getReminder()
            if (preference.isEnabled) reminderScheduler.schedule(preference)
        }

    override suspend fun clearReminder(): Result<Unit> =
        suspendRunCatching {
            reminderScheduler.cancel()
            localDataSource.clearReminder()
        }
}
