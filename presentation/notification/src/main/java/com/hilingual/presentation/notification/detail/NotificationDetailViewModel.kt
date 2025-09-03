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
                .onLogFailure {
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
