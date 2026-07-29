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
package com.hilingual.presentation.notification.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.notification.main.model.toFeedStateOrNull
import com.hilingual.presentation.notification.main.model.toNoticeStateOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private fun loadTab(tab: NotificationTab, isRefreshing: Boolean) {
        viewModelScope.launch {
            setLoadFailed(tab, isFailed = false)
            if (isRefreshing) {
                setRefreshing(tab, isRefreshing = true)
            }
            userRepository.getNotifications(tab.name)
                .onSuccess { notifications ->
                    _uiState.update {
                        when (tab) {
                            NotificationTab.FEED -> it.copy(
                                feedNotifications = notifications.mapNotNull { item -> item.toFeedStateOrNull() }
                                    .toImmutableList(),
                            )

                            NotificationTab.NOTIFICATION -> it.copy(
                                noticeNotifications = notifications.mapNotNull { item -> item.toNoticeStateOrNull() }
                                    .toImmutableList(),
                            )
                        }
                    }
                }
                .onLogFailure {
                    setLoadFailed(tab, isFailed = true)
                }

            if (isRefreshing) {
                setRefreshing(tab, isRefreshing = false)
            }
        }
    }

    fun loadTab(tab: NotificationTab) {
        loadTab(tab, isRefreshing = false)
    }

    fun refreshTab(tab: NotificationTab) {
        loadTab(tab, isRefreshing = true)
    }

    fun readNotification(noticeId: Long) {
        viewModelScope.launch {
            userRepository.readNotification(noticeId)
                .onSuccess {
                    val updatedFeeds = _uiState.value.feedNotifications.map {
                        if (it.id == noticeId) it.copy(isRead = true) else it
                    }.toImmutableList()
                    _uiState.update { it.copy(feedNotifications = updatedFeeds) }
                }
                .onLogFailure { /* TODO: 에러 처리 */ }
        }
    }

    private fun setRefreshing(tab: NotificationTab, isRefreshing: Boolean) {
        _uiState.update {
            when (tab) {
                NotificationTab.FEED -> it.copy(isFeedRefreshing = isRefreshing)
                NotificationTab.NOTIFICATION -> it.copy(isNoticeRefreshing = isRefreshing)
            }
        }
    }

    private fun setLoadFailed(tab: NotificationTab, isFailed: Boolean) {
        _uiState.update {
            it.copy(
                failedTabs = if (isFailed) {
                    it.failedTabs.adding(tab)
                } else {
                    it.failedTabs.removing(tab)
                },
            )
        }
    }
}
