package com.hilingual.data.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hilingual.core.notification.HilingualNotificationManager
import com.hilingual.data.notification.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class ReminderAlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderRepository: ReminderRepository

    @Inject lateinit var notificationManager: HilingualNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        launchAsync {
            reminderRepository.rescheduleIfEnabled().onFailure(Timber::e)
            notificationManager.showCustomReminderNotification()
        }
    }
}
