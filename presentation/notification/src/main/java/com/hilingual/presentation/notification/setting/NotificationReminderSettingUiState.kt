package com.hilingual.presentation.notification.setting

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.notification.setting.model.ReminderDay

@Immutable
internal data class NotificationReminderSettingUiState(
    val hour: Int = 0,
    val minute: Int = 0,
    val isDailyRepeat: Boolean = true,
    val selectedDays: Set<ReminderDay> = ReminderDay.entries.toSet(),
)
