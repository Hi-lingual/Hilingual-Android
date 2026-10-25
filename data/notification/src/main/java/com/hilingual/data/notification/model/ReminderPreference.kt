package com.hilingual.data.notification.model

import java.time.DayOfWeek
import java.time.ZonedDateTime

data class ReminderPreference(
    val isEnabled: Boolean = false,
    val hour: Int = 0,
    val minute: Int = 0,
    val isDailyRepeat: Boolean = true,
    val selectedDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
) {
    fun nextTriggerAt(now: ZonedDateTime): ZonedDateTime? {
        val days = if (isDailyRepeat) DayOfWeek.entries.toSet() else selectedDays
        if (days.isEmpty()) return null

        return (0L..7L).asSequence()
            .map { offset -> now.toLocalDate().plusDays(offset).atTime(hour, minute).atZone(now.zone) }
            .firstOrNull { candidate -> candidate.dayOfWeek in days && candidate.isAfter(now) }
    }
}
