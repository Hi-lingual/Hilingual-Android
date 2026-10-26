package com.hilingual.presentation.notification.setting.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.notification.model.ReminderPreference

@Immutable
internal data class ReminderUiModel(
    val isEnabled: Boolean = false,
    val hour: Int = 0,
    val minute: Int = 0,
    val selectedDays: Set<ReminderDay> = ReminderDay.entries.toSet(),
) {
    val scheduleText: String
        get() {
            val dayText = if (selectedDays.size == ReminderDay.entries.size) {
                "매일"
            } else {
                selectedDays.sortedBy { it.ordinal }.joinToString(", ") { it.label }
            }

            val period = if (hour < 12) "오전" else "오후"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }

            return "$dayText · $period $displayHour:${minute.toString().padStart(2, '0')}"
        }
}

internal fun ReminderPreference.toUiModel() = ReminderUiModel(
    isEnabled = isEnabled,
    hour = hour,
    minute = minute,
    selectedDays = selectedDays.map { it.toReminderDay() }.toSet(),
)
