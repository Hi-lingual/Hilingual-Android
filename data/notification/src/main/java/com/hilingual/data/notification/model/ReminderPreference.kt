package com.hilingual.data.notification.model

import java.time.DayOfWeek

data class ReminderPreference(
    val isEnabled: Boolean = false,
    val hour: Int = 0,
    val minute: Int = 0,
    val isDailyRepeat: Boolean = true,
    val selectedDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
)
