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
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.notification.main.model.toFeedStateOrNull
import com.hilingual.presentation.notification.main.model.toNoticeStateOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun requestTab(tab: NotificationTab, isRefreshing: Boolean) {
        viewModelScope.launch {
            val shouldShowLoadError = _uiState.value.tabState(tab).loadState !is UiState.Success
            _uiState.update {
                it.updateTabState(tab) { tabState ->
                    tabState.copy(
                        loadState = if (shouldShowLoadError) UiState.Loading else tabState.loadState,
                        isRefreshing = isRefreshing,
                    )
                }
            }

            userRepository.getNotifications(tab.name)
                .onSuccess { notifications ->
                    _uiState.update { currentState ->
                        val successState = when (tab) {
                            NotificationTab.FEED -> currentState.copy(
                                feedNotifications = notifications.mapNotNull { item -> item.toFeedStateOrNull() }
                                    .toImmutableList(),
                            )

                            NotificationTab.NOTIFICATION -> currentState.copy(
                                noticeNotifications = notifications.mapNotNull { item -> item.toNoticeStateOrNull() }
                                    .toImmutableList(),
                            )
                        }
                        successState.updateTabState(tab) {
                            it.copy(
                                loadState = UiState.Success(Unit),
                                isRefreshing = false,
                            )
                        }
                    }
                }
                .onLogFailure {
                    _uiState.update {
                        it.updateTabState(tab) { tabState ->
                            tabState.copy(
                                loadState = if (shouldShowLoadError) {
                                    UiState.Failure(LoadErrorHandleAction.Retry)
                                } else {
                                    tabState.loadState
                                },
                                isRefreshing = false,
                            )
                        }
                    }
                    if (!shouldShowLoadError) {
                        _sideEffect.emit(NotificationSideEffect.ShowErrorDialog(tab))
                    }
                }
        }
    }

    fun loadTab(tab: NotificationTab) {
        when (_uiState.value.tabState(tab).loadState) {
            UiState.Empty,
            is UiState.Failure,
            -> requestTab(tab, isRefreshing = false)

            UiState.Loading,
            is UiState.Success,
            -> return
        }
    }

    fun refreshTab(tab: NotificationTab) {
        requestTab(tab, isRefreshing = true)
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
}

sealed interface NotificationSideEffect {
    data class ShowErrorDialog(val tab: NotificationTab) : NotificationSideEffect
}
