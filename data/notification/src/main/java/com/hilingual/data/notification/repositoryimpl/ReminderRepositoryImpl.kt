package com.hilingual.data.notification.repositoryimpl

import com.hilingual.data.notification.datasource.ReminderLocalDataSource
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.reminder.ReminderScheduler
import com.hilingual.data.notification.repository.ReminderRepository
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val localDataSource: ReminderLocalDataSource,
    private val reminderScheduler: ReminderScheduler,
) : ReminderRepository {

    override suspend fun getReminder(): ReminderPreference =
        localDataSource.getReminder()

    override suspend fun saveReminder(preference: ReminderPreference) {
        localDataSource.saveReminder(preference)
        reminderScheduler.schedule(preference)
    }

    override suspend fun disableReminder() {
        localDataSource.updateReminderEnabled(false)
        reminderScheduler.cancel()
    }

    override suspend fun rescheduleIfEnabled() {
        val preference = getReminder()
        if (preference.isEnabled) reminderScheduler.schedule(preference)
    }
}
