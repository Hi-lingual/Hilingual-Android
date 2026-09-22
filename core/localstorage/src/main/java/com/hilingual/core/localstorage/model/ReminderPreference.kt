package com.hilingual.core.localstorage.model

data class ReminderPreference(
    val isEnabled: Boolean = false,
    val hour: Int = 21,
    val minute: Int = 0,
    val isDailyRepeat: Boolean = true,
    val selectedDays: Set<String> = emptySet(),
)
