/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.notification.main.tab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hilingual.presentation.notification.main.component.EmptyImage
import com.hilingual.presentation.notification.main.component.NotificationItem
import com.hilingual.presentation.notification.main.model.FeedNotificationItemModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun FeedScreen(
    notifications: ImmutableList<FeedNotificationItemModel>,
    onNotificationClick: (FeedNotificationItemModel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (notifications.isEmpty()) {
        EmptyImage()
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(
                items = notifications,
                key = { it.noticeId }
            ) { notification ->
                NotificationItem(
                    title = notification.title,
                    date = notification.date,
                    isRead = notification.isRead,
                    onClick = { onNotificationClick(notification) }
                )
            }
        }
    }
}
