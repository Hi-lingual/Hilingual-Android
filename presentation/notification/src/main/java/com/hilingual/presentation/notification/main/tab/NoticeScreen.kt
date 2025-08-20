package com.hilingual.presentation.notification.main.tab

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hilingual.presentation.notification.main.component.EmptyImage
import com.hilingual.presentation.notification.main.component.NotificationItem
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun NoticeScreen(
    notifications: ImmutableList<NoticeNotificationItemModel>,
    onNotificationClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (notifications.isEmpty()) {
        EmptyImage()
    } else {
        LazyColumn(modifier = modifier) {
            items(
                items = notifications,
                key = { it.id }
            ) { notification ->
                NotificationItem(
                    title = notification.title,
                    date = notification.date,
                    isRead = notification.isRead,
                    onClick = { onNotificationClick(notification.id) }
                )
            }
        }
    }
}
