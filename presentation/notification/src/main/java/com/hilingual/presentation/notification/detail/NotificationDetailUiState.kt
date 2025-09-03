package com.hilingual.presentation.notification.detail

import androidx.compose.runtime.Immutable

@Immutable
internal data class NotificationDetailUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val date: String = "",
    val content: String = ""
)
