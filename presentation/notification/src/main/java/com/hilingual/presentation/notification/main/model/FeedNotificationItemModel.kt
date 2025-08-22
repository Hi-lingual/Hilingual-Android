package com.hilingual.presentation.notification.main.model

import androidx.compose.runtime.Immutable

@Immutable
data class FeedNotificationItemModel(
    val id: Long,
    val title: String,
    val date: String,
    val isRead: Boolean,
    val deepLink: String
)
