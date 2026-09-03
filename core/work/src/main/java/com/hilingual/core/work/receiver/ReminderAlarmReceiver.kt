package com.hilingual.core.work.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hilingual.core.localstorage.datasource.ReminderPreferenceDataSource
import com.hilingual.core.notification.HilingualNotificationManager
import com.hilingual.core.work.scheduler.ReminderScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReminderAlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderPreferenceDataSource: ReminderPreferenceDataSource
    @Inject lateinit var reminderScheduler: ReminderScheduler
    @Inject lateinit var notificationManager: HilingualNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationManager.showDiaryReminderNotification()

                val preference = reminderPreferenceDataSource.reminderFlow.first()
                if (preference.isEnabled) {
                    reminderScheduler.schedule(preference)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
