package com.hilingual.presentation.notification.setting

import androidx.compose.runtime.Immutable

@Immutable
internal data class NotificationSettingUiState(
    val isMarketingChecked: Boolean = false,
    val isFeedChecked: Boolean = false
)
