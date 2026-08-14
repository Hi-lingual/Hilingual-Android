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
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.hilingual.core.designsystem.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class HilingualNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val notificationManager: NotificationManager? = context.getSystemService()

    fun createNotificationChannels() {
        val dailyChannel = NotificationChannel(
            CHANNEL_ID_DAILY,
            "일간 알림",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "하루를 정리하는 알림"
        }

        val weeklyChannel = NotificationChannel(
            CHANNEL_ID_WEEKLY,
            "주간 알림",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "한 주를 정리하는 알림"
        }

        val socialChannel = NotificationChannel(
            CHANNEL_ID_SOCIAL,
            "소셜 알림",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "팔로우, 좋아요 등 소셜 활동 알림"
        }

        notificationManager?.createNotificationChannels(
            listOf(dailyChannel, weeklyChannel, socialChannel),
        )
    }

    fun sendReminderNotification(
        channelId: String?,
        title: String,
        message: String,
        deepLink: String? = null,
    ) {
        val targetChannelId = channelId?.takeIf { it in KNOWN_CHANNEL_IDS } ?: CHANNEL_ID_SOCIAL
        if (channelId != null && channelId !in KNOWN_CHANNEL_IDS) {
            Timber.e("Unknown channelId from server: $channelId")
        }

        showReminderNotification(
            channelId = targetChannelId,
            notificationId = resolveNotificationId(targetChannelId),
            title = title,
            message = message,
            deepLink = deepLink,
        )
    }

    private fun resolveNotificationId(channelId: String): Int = when (channelId) {
        CHANNEL_ID_DAILY -> NOTIFICATION_ID_DAILY
        CHANNEL_ID_WEEKLY -> NOTIFICATION_ID_WEEKLY
        else -> System.currentTimeMillis().toInt()
    }

    private fun showReminderNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        deepLink: String? = null,
    ) {
        val pendingIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                deepLink?.takeIf { it.isNotBlank() }?.let { putExtra("link", it) }
                putExtra(EXTRA_NOTIFICATION_TYPE, channelId.toNotificationType())
            }
            ?.let {
                PendingIntent.getActivity(
                    context,
                    notificationId,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

        val isSocial = channelId == CHANNEL_ID_SOCIAL

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .apply {
                if (isSocial) {
                    setGroup(GROUP_KEY_SOCIAL)
                }
            }

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        } else {
            Timber.e("PendingIntent is null.")
        }

        if (notificationManager?.areNotificationsEnabled() != true) {
            Timber.e("Notifications are disabled for this app.")
            return
        }

        notificationManager.notify(notificationId, builder.build())

        if (isSocial) {
            notifySocialSummary()
        }
    }

    private fun notifySocialSummary() {
        val summaryPendingIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            ?.let {
                PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID_SOCIAL_SUMMARY,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

        val summary = NotificationCompat.Builder(context, CHANNEL_ID_SOCIAL)
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(NotificationCompat.InboxStyle())
            .setGroup(GROUP_KEY_SOCIAL)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .apply {
                if (summaryPendingIntent != null) {
                    setContentIntent(summaryPendingIntent)
                } else {
                    Timber.e("Summary PendingIntent is null.")
                }
            }
            .build()

        notificationManager?.notify(NOTIFICATION_ID_SOCIAL_SUMMARY, summary)
    }

    private fun String.toNotificationType(): String = when (this) {
        CHANNEL_ID_DAILY -> "daily"
        CHANNEL_ID_WEEKLY -> "weekly"
        else -> "social"
    }

    companion object {
        private const val CHANNEL_ID_DAILY = "channel_daily_notification"
        private const val CHANNEL_ID_WEEKLY = "channel_weekly_notification"
        private const val CHANNEL_ID_SOCIAL = "channel_social_notification"

        private val KNOWN_CHANNEL_IDS = setOf(CHANNEL_ID_DAILY, CHANNEL_ID_WEEKLY, CHANNEL_ID_SOCIAL)

        private const val NOTIFICATION_ID_DAILY = 1001
        private const val NOTIFICATION_ID_WEEKLY = 1002
        private const val NOTIFICATION_ID_SOCIAL_SUMMARY = 1003

        private const val GROUP_KEY_SOCIAL = "group_social_notification"

        /** 푸시 알림 클릭 이벤트의 notification_type 프로퍼티로 전달된다. */
        const val EXTRA_NOTIFICATION_TYPE = "notification_type"
    }
}
