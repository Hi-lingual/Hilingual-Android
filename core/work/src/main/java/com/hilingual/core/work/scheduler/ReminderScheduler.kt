package com.hilingual.core.work.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.getSystemService
import com.hilingual.core.localstorage.model.ReminderPreference
import com.hilingual.core.work.receiver.ReminderAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
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
        val days = if (preference.isDailyRepeat) ALL_DAYS else preference.selectedDays
        if (days.isEmpty()) return null

        val now = Calendar.getInstance()
        for (offset in 0..7) {
            val candidate = (now.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, offset)
                set(Calendar.HOUR_OF_DAY, preference.hour)
                set(Calendar.MINUTE, preference.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (candidate.toDayName() in days && candidate.timeInMillis > now.timeInMillis) {
                return candidate.timeInMillis
            }
        }
        return null
    }

    private fun Calendar.toDayName(): String = when (get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "SUN"
        Calendar.MONDAY -> "MON"
        Calendar.TUESDAY -> "TUE"
        Calendar.WEDNESDAY -> "WED"
        Calendar.THURSDAY -> "THU"
        Calendar.FRIDAY -> "FRI"
        else -> "SAT"
    }

    companion object {
        private const val REQUEST_CODE = 5001
        private val ALL_DAYS = setOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    }
}
