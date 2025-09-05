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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hilingual.presentation.notification.main.component.EmptyImage
import com.hilingual.presentation.notification.main.component.NotificationItem
import com.hilingual.presentation.notification.main.model.FeedNotificationItemUiModel
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedScreen(
    notifications: ImmutableList<FeedNotificationItemUiModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    listState: LazyListState,
    onNotificationClick: (FeedNotificationItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = if (notifications.isEmpty()) Arrangement.Center else Arrangement.Top
        ) {
            if (notifications.isEmpty()) {
                item {
                    EmptyImage(modifier = Modifier.fillParentMaxSize())
                }
            } else {
                items(
                    items = notifications,
                    key = { it.id }
                ) { notification ->
                    NotificationItem(
                        title = notification.title,
                        date = notification.publishedAt,
                        isRead = notification.isRead,
                        onClick = { onNotificationClick(notification) }
                    )
                }
            }
        }
    }
}
