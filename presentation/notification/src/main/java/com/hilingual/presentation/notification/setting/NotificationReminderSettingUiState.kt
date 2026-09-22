package com.hilingual.presentation.notification.setting

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.notification.setting.component.DayOfWeek

@Immutable
internal data class NotificationReminderSettingUiState(
    val hour: Int = 0,
    val minute: Int = 0,
    val isDailyRepeat: Boolean = false,
    val selectedDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
)
