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
package com.hilingual.presentation.notification.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.notification.navigation.NotificationDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val noticeId: Long = savedStateHandle.toRoute<NotificationDetail>().noticeId

    private val _uiState = MutableStateFlow(NotificationDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getNotificationDetail()
    }

    private fun getNotificationDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getNotificationDetail(noticeId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            title = detail.title,
                            date = detail.createdAt,
                            content = detail.content
                        )
                    }
                }
                .onLogFailure { }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
