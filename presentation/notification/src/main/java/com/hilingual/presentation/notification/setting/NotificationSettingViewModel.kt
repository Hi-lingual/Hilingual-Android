package com.hilingual.presentation.notification.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class NotificationSettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<NotificationSettingUiState>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val serverState = MutableStateFlow(NotificationSettingUiState())

    private val marketingToggleFlow = MutableSharedFlow<Boolean>(replay = 1)
    private val feedToggleFlow = MutableSharedFlow<Boolean>(replay = 1)

    init {
        getNotificationSettings()
        observeMarketingToggle()
        observeFeedToggle()
    }

    private fun getNotificationSettings() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            userRepository.getNotificationSettings()
                .onSuccess { settings ->
                    val newUiState = NotificationSettingUiState(
                        isMarketingChecked = settings.isMarketingEnabled,
                        isFeedChecked = settings.isFeedEnabled
                    )
                    _uiState.update { UiState.Success(newUiState) }
                    serverState.update { newUiState }
                }
                .onLogFailure { /* TODO: 에러 처리 */ }
        }
    }

    private fun observeMarketingToggle() {
        marketingToggleFlow
            .debounce(400L)
            .filter { isChecked -> serverState.value.isMarketingChecked != isChecked }
            .onEach { updateNotificationSetting(NotiType.MARKETING) }
            .launchIn(viewModelScope)
    }

    private fun observeFeedToggle() {
        feedToggleFlow
            .debounce(400L)
            .filter { isChecked -> serverState.value.isFeedChecked != isChecked }
            .onEach { updateNotificationSetting(NotiType.FEED) }
            .launchIn(viewModelScope)
    }

    fun updateMarketingChecked(isChecked: Boolean) {
        val currentUiState = _uiState.value
        if (currentUiState !is UiState.Success) return

        _uiState.update { UiState.Success(currentUiState.data.copy(isMarketingChecked = isChecked)) }
        viewModelScope.launch { marketingToggleFlow.emit(isChecked) }
    }

    fun updateFeedChecked(isChecked: Boolean) {
        val currentUiState = _uiState.value
        if (currentUiState !is UiState.Success) return

        _uiState.update { UiState.Success(currentUiState.data.copy(isFeedChecked = isChecked)) }
        viewModelScope.launch { feedToggleFlow.emit(isChecked) }
    }

    private fun updateNotificationSetting(notiType: NotiType) {
        viewModelScope.launch {
            userRepository.updateNotificationSetting(notiType.name)
                .onSuccess { settings ->
                    val newUiState = NotificationSettingUiState(
                        isMarketingChecked = settings.isMarketingEnabled,
                        isFeedChecked = settings.isFeedEnabled
                    )
                    _uiState.update { UiState.Success(newUiState) }
                    serverState.update { newUiState }
                }
                .onLogFailure {
                    _uiState.update { UiState.Success(serverState.value) }
                }
        }
    }
}

private enum class NotiType {
    FEED,
    MARKETING
}
