package com.hilingual.data.notification.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.getSystemService
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.receiver.ReminderAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val alarmManager: AlarmManager? = context.getSystemService()

    fun schedule(preference: ReminderPreference) {
        cancel()
        if (!preference.isEnabled) return

        val triggerAtMillis = nextTriggerMillis(preference) ?: return
        val pendingIntent = buildPendingIntent()
        val manager = alarmManager ?: return

        val canScheduleExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            manager.canScheduleExactAlarms()
        } else {
            true
        }

        if (canScheduleExact) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun cancel() {
        alarmManager?.cancel(buildPendingIntent())
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(context, ReminderAlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun nextTriggerMillis(preference: ReminderPreference): Long? {
        val days = if (preference.isDailyRepeat) DayOfWeek.entries.toSet() else preference.selectedDays
        if (days.isEmpty()) return null

        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        for (offset in 0L..7L) {
            val candidate = now.toLocalDate()
                .plusDays(offset)
                .atTime(preference.hour, preference.minute)
                .atZone(zone)
            if (candidate.dayOfWeek in days && candidate.isAfter(now)) {
                return candidate.toInstant().toEpochMilli()
            }
        }
        return null
    }

    companion object {
        private const val REQUEST_CODE = 5001
    }
}
