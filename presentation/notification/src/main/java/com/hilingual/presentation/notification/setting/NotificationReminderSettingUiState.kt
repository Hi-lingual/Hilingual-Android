package com.hilingual.presentation.notification.setting

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.notification.setting.model.ReminderDay

@Immutable
internal data class NotificationReminderSettingUiState(
    val hour: Int = 0,
    val minute: Int = 0,
    val selectedDays: Set<ReminderDay> = ReminderDay.entries.toSet(),
) {
    val isDailyRepeat: Boolean
        get() = selectedDays.size == ReminderDay.entries.size
}
