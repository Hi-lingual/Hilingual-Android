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
package com.hilingual.presentation.notification.setting

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.util.UiState
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.repository.NotificationRepository
import com.hilingual.data.notification.repository.ReminderRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.notification.setting.model.ReminderActivationStep
import com.hilingual.presentation.notification.setting.model.toReminderDay
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
internal class NotificationSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reminderRepository: ReminderRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<NotificationSettingUiState>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationSettingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _isNotificationGranted = MutableStateFlow<Boolean?>(null)
    val isNotificationGranted = _isNotificationGranted.asStateFlow()

    private val _isExactAlarmGranted = MutableStateFlow(false)
    val isExactAlarmGranted = _isExactAlarmGranted.asStateFlow()

    private var isAwaitingExactAlarmPermission = false

    private val serverState = MutableStateFlow(ServerSettings())

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
                    val server = ServerSettings(
                        isMarketingChecked = settings.isMarketingEnabled,
                        isFeedChecked = settings.isFeedEnabled,
                    )
                    serverState.update { server }

                    reminderRepository.getReminder()
                        .onSuccess { reminder ->
                            val newUiState = NotificationSettingUiState(
                                isMarketingChecked = server.isMarketingChecked,
                                isFeedChecked = server.isFeedChecked,
                            ).withReminder(reminder)
                            _uiState.update { UiState.Success(newUiState) }
                        }
                        .onLogFailure {
                            _uiState.update { UiState.Failure(LoadErrorHandleAction.Retry) }
                        }
                }
                .onLogFailure {
                    _uiState.update { UiState.Failure(LoadErrorHandleAction.Retry) }
                }
        }
    }

    fun retryLoad() = getNotificationSettings()

    fun updatePermissionState(isNotificationGranted: Boolean) {
        _isNotificationGranted.update { isNotificationGranted }
        _isExactAlarmGranted.update { reminderRepository.isExactAlarmAllowed() }

        if (isAwaitingExactAlarmPermission) {
            isAwaitingExactAlarmPermission = false
            if (_isExactAlarmGranted.value) startReminderActivation()
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

        if (_isNotificationGranted.value == false) {
            viewModelScope.launch { _sideEffect.emit(NotificationSettingSideEffect.ShowPermissionDialog) }
            return
        }

        _uiState.update { UiState.Success(currentUiState.data.copy(isMarketingChecked = isChecked)) }
        viewModelScope.launch { marketingToggleFlow.emit(isChecked) }
    }

    fun updateFeedChecked(isChecked: Boolean) {
        val currentUiState = _uiState.value
        if (currentUiState !is UiState.Success) return

        if (_isNotificationGranted.value == false) {
            viewModelScope.launch { _sideEffect.emit(NotificationSettingSideEffect.ShowPermissionDialog) }
            return
        }

        _uiState.update { UiState.Success(currentUiState.data.copy(isFeedChecked = isChecked)) }
        viewModelScope.launch { feedToggleFlow.emit(isChecked) }
    }

    fun onReminderCheckedChange(isChecked: Boolean) {
        if (isChecked) startReminderActivation() else disableReminder()
    }

    fun onReminderSectionClick() = startReminderActivation()

    private fun startReminderActivation() {
        val step = ReminderActivationStep.resolve(
            isNotificationGranted = _isNotificationGranted.value == true,
            isExactAlarmGranted = _isExactAlarmGranted.value,
        )
        val sideEffect = when (step) {
            ReminderActivationStep.REQUIRE_NOTIFICATION_PERMISSION ->
                NotificationSettingSideEffect.ShowReminderPermissionDialog

            ReminderActivationStep.REQUIRE_EXACT_ALARM_PERMISSION ->
                NotificationSettingSideEffect.ShowExactAlarmPermissionDialog

            ReminderActivationStep.READY -> NotificationSettingSideEffect.NavigateToReminderSetting
        }
        viewModelScope.launch { _sideEffect.emit(sideEffect) }
    }

    fun onReminderPermissionDialogConfirm() {
        viewModelScope.launch {
            val isPermanentlyDenied = notificationRepository.getIsNotificationPermissionPermanentlyDenied()
                .getOrDefault(false)
            val canRequestPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isPermanentlyDenied
            _sideEffect.emit(
                if (canRequestPermission) {
                    NotificationSettingSideEffect.RequestNotificationPermission
                } else {
                    NotificationSettingSideEffect.OpenNotificationSettings
                },
            )
        }
    }

    fun onNotificationPermissionResult(isGranted: Boolean, shouldShowRationale: Boolean) {
        if (isGranted) {
            _isNotificationGranted.update { true }
            startReminderActivation()
            return
        }
        if (!shouldShowRationale) {
            viewModelScope.launch {
                notificationRepository.updateIsNotificationPermissionPermanentlyDenied(true)
                    .onLogFailure { }
            }
        }
    }

    fun onExactAlarmPermissionDialogConfirm() {
        isAwaitingExactAlarmPermission = true
        viewModelScope.launch { _sideEffect.emit(NotificationSettingSideEffect.OpenExactAlarmSettings) }
    }

    private fun disableReminder() {
        viewModelScope.launch {
            reminderRepository.disableReminder()
                .onSuccess { _uiState.updateSuccess { it.copy(isReminderChecked = false) } }
                .onLogFailure { _sideEffect.emit(NotificationSettingSideEffect.ShowToast("리마인드 알림을 해제하지 못했어요.")) }
        }
    }

    fun refreshReminderState() {
        viewModelScope.launch {
            reminderRepository.getReminder()
                .onSuccess { reminder -> _uiState.updateSuccess { it.withReminder(reminder) } }
                .onLogFailure { }
        }
    }

    private fun updateNotificationSetting(notiType: NotiType) {
        viewModelScope.launch {
            userRepository.updateNotificationSetting(notiType.name)
                .onSuccess { settings ->
                    val server = ServerSettings(
                        isMarketingChecked = settings.isMarketingEnabled,
                        isFeedChecked = settings.isFeedEnabled,
                    )
                    serverState.update { server }
                    _uiState.updateSuccess {
                        it.copy(
                            isMarketingChecked = server.isMarketingChecked,
                            isFeedChecked = server.isFeedChecked,
                        )
                    }
                }
                .onLogFailure {
                    val server = serverState.value
                    _uiState.updateSuccess {
                        it.copy(
                            isMarketingChecked = server.isMarketingChecked,
                            isFeedChecked = server.isFeedChecked,
                        )
                    }
                }
        }
    }

    private fun NotificationSettingUiState.withReminder(reminder: ReminderPreference) = copy(
        isReminderChecked = reminder.isEnabled,
        reminderHour = reminder.hour,
        reminderMinute = reminder.minute,
        selectedDays = reminder.selectedDays.map { it.toReminderDay() }.toSet(),
    )
}

private data class ServerSettings(
    val isMarketingChecked: Boolean = false,
    val isFeedChecked: Boolean = false,
)

private enum class NotiType {
    FEED,
    MARKETING,
}

internal sealed interface NotificationSettingSideEffect {
    data object ShowPermissionDialog : NotificationSettingSideEffect
    data object ShowReminderPermissionDialog : NotificationSettingSideEffect
    data object RequestNotificationPermission : NotificationSettingSideEffect
    data object OpenNotificationSettings : NotificationSettingSideEffect
    data object ShowExactAlarmPermissionDialog : NotificationSettingSideEffect
    data object OpenExactAlarmSettings : NotificationSettingSideEffect
    data object NavigateToReminderSetting : NotificationSettingSideEffect
    data class ShowToast(val text: String) : NotificationSettingSideEffect
}
