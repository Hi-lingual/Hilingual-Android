package com.hilingual.data.notification.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.notification.datasource.ReminderLocalDataSource
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.repository.ReminderRepository
import com.hilingual.data.notification.scheduler.ReminderScheduler
import javax.inject.Inject
import timber.log.Timber

internal class ReminderRepositoryImpl @Inject constructor(
    private val localDataSource: ReminderLocalDataSource,
    private val reminderScheduler: ReminderScheduler,
) : ReminderRepository {

    override fun isExactAlarmAllowed(): Boolean = reminderScheduler.canScheduleExactAlarms()

    override suspend fun getReminder(): Result<ReminderPreference> =
        suspendRunCatching { localDataSource.getReminder() }

    override suspend fun saveReminder(preference: ReminderPreference): Result<Unit> =
        suspendRunCatching {
            check(isExactAlarmAllowed()) { "Exact alarm permission is not granted." }
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
            when {
                !preference.isEnabled -> Unit
                !isExactAlarmAllowed() -> Timber.w("Skip reminder reschedule: exact alarm permission is not granted.")
                else -> reminderScheduler.schedule(preference)
            }
        }

    override suspend fun clearReminder(): Result<Unit> =
        suspendRunCatching {
            reminderScheduler.cancel()
            localDataSource.clearReminder()
        }
}
