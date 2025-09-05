package com.hilingual.presentation.notification.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.notification.main.model.toFeedUiModel
import com.hilingual.presentation.notification.main.model.toNoticeUiModel
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

    fun onTabSelected(tab: NotificationTab) {
        viewModelScope.launch {
            loadTab(tab)
        }
    }

    fun refresh(tab: NotificationTab) {
        viewModelScope.launch {
            setRefreshing(tab, isRefreshing = true)
            loadTab(tab)
            setRefreshing(tab, isRefreshing = false)
        }
    }

    private suspend fun loadTab(tab: NotificationTab) {
        userRepository.getNotifications(tab.name)
            .onSuccess { notifications ->
                _uiState.update {
                    when (tab) {
                        NotificationTab.FEED -> it.copy(feedNotifications = notifications.map { item -> item.toFeedUiModel() }
                            .toImmutableList())

                        NotificationTab.NOTIFICATION -> it.copy(noticeNotifications = notifications.map { item -> item.toNoticeUiModel() }
                            .toImmutableList())
                    }
                }
            }
            .onLogFailure { /* TODO: 에러 처리 */ }
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
}