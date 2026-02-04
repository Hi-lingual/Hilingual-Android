/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.hilingual.core.designsystem.R
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HilingualNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager: NotificationManager? = context.getSystemService()

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val dailyChannel = NotificationChannel(
            CHANNEL_ID_DAILY,
            "일간 알림",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "하루를 정리하는 알림"
        }

        val weeklyChannel = NotificationChannel(
            CHANNEL_ID_WEEKLY,
            "주간 알림",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "한 주를 정리하는 알림"
        }

        notificationManager?.createNotificationChannels(listOf(dailyChannel, weeklyChannel))
    }

    fun sendReminderNotification(channelId: String?, title: String, message: String) {
        val targetChannelId = channelId ?: CHANNEL_ID_DAILY
        showReminderNotification(
            channelId = targetChannelId,
            notificationId = System.currentTimeMillis().toInt(),
            title = title,
            message = message
        )
    }

    private fun showReminderNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String
    ) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)

        val pendingIntent = launchIntent?.let {
            PendingIntent.getActivity(
                context,
                notificationId,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        } else {
            Timber.e("Launch Intent is null. Cannot set ContentIntent.")
        }

        try {
            notificationManager?.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            Timber.e(e, "Failed to send notification due to permission issues.")
        }
    }

    companion object {
        private const val CHANNEL_ID_DAILY = "channel_daily_notification"
        private const val CHANNEL_ID_WEEKLY = "channel_weekly_notification"
    }
}
