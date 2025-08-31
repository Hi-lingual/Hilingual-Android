package com.hilingual.presentation.notification.main

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.notification.main.model.FeedNotificationItemModel
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class NotificationMainUiState(
    val feedNotifications: ImmutableList<FeedNotificationItemModel> = persistentListOf(),
    val noticeNotifications: ImmutableList<NoticeNotificationItemModel> = persistentListOf()
)
