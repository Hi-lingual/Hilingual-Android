package com.hilingual.presentation.notification.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.notification.main.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            userRepository.getFeedNotifications()
                .onSuccess { notifications ->
                    val uiModels = notifications.map { it.toUiModel() }.toImmutableList()
                    _uiState.update {
                        it.copy(feedNotifications = uiModels, isRefreshing = false)
                    }
                }
                .onLogFailure {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
        }
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getFeedNotifications()
                .onSuccess { notifications ->
                    val uiModels = notifications.map { it.toUiModel() }.toImmutableList()
                    _uiState.update {
                        it.copy(feedNotifications = uiModels, isLoading = false)
                    }
                }
                .onLogFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun refreshNotice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            userRepository.getNoticeNotifications()
                .onSuccess { notifications ->
                    val uiModels = notifications.map { it.toUiModel() }.toImmutableList()
                    _uiState.update {
                        it.copy(noticeNotifications = uiModels, isRefreshing = false)
                    }
                }
                .onLogFailure {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
        }
    }

    fun loadNotice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getNoticeNotifications()
                .onSuccess { notifications ->
                    val uiModels = notifications.map { it.toUiModel() }.toImmutableList()
                    _uiState.update {
                        it.copy(noticeNotifications = uiModels, isLoading = false)
                    }
                }
                .onLogFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun readFeedNotification(noticeId: Long) {
        viewModelScope.launch {
            userRepository.readNotification(noticeId)
                .onSuccess {
                    val updatedFeeds = _uiState.value.feedNotifications.map {
                        if (it.noticeId == noticeId) it.copy(isRead = true) else it
                    }.toImmutableList()
                    _uiState.update { it.copy(feedNotifications = updatedFeeds) }
                }
                .onLogFailure {
                }
        }
    }
}
