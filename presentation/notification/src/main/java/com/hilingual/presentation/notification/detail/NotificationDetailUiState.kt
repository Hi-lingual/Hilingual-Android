package com.hilingual.presentation.notification.detail

import androidx.compose.runtime.Immutable

@Immutable
internal data class NotificationDetailUiState(
    val noticeDetailTitle: String = "",
    val noticeDetailDate: String = "",
    val noticeDetailContent: String = ""
)
