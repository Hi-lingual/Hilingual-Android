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
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "하루를 정리하는 알림"
        }

        val weeklyChannel = NotificationChannel(
            CHANNEL_ID_WEEKLY,
            "주간 알림",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "한 주를 정리하는 알림"
        }

        notificationManager?.createNotificationChannels(listOf(dailyChannel, weeklyChannel))
    }

    fun sendDailyNotification() {
        sendNotification(
            channelId = CHANNEL_ID_DAILY,
            notificationId = NOTIFICATION_ID_DAILY,
            title = "하루를 정리해 볼 시간 ✏️",
            message = "오늘 하루를 돌아보며 떠오르는 생각들을 자유롭게 적어보세요."
        )
    }

    fun sendWeeklyNotification() {
        sendNotification(
            channelId = CHANNEL_ID_WEEKLY,
            notificationId = NOTIFICATION_ID_WEEKLY,
            title = "한 주를 정리해보는 시간 ✍️",
            message = "특별한 주제가 없어도 괜찮아요. 지금 생각나는 걸 써보세요."
        )
    }

    private fun sendNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String
    ) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = launchIntent?.let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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
        
        private const val NOTIFICATION_ID_DAILY = 1001
        private const val NOTIFICATION_ID_WEEKLY = 1002
    }
}
