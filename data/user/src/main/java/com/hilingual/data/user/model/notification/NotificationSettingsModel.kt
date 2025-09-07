package com.hilingual.data.user.model.notification

import com.hilingual.data.user.dto.response.notification.NotificationSettingsResponseDto

data class NotificationSettingsModel(
    val isMarketingEnabled: Boolean,
    val isFeedEnabled: Boolean
)

internal fun NotificationSettingsResponseDto.toModel() = NotificationSettingsModel(
    isMarketingEnabled = this.marketing,
    isFeedEnabled = this.feed
)
