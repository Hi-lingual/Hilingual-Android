package com.hilingual.presentation.notification.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.util.UiState
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.repository.ReminderRepository
import com.hilingual.presentation.notification.setting.model.ReminderDay
import com.hilingual.presentation.notification.setting.model.toReminderDay
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class NotificationReminderSettingViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<NotificationReminderSettingUiState>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationReminderSettingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var savedState: NotificationReminderSettingUiState? = null
    private var saveJob: Job? = null
    private var isNavigatingUp = false

    init {
        loadReminder()
    }

    fun retryLoad() = loadReminder()

    private fun loadReminder() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            reminderRepository.getReminder()
                .onSuccess { pref ->
                    val initial = NotificationReminderSettingUiState(
                        hour = pref.hour,
                        minute = pref.minute,
                        selectedDays = pref.selectedDays.map { it.toReminderDay() }.toSet(),
                    )
                    savedState = initial
                    _uiState.update { UiState.Success(initial) }
                }
                .onLogFailure {
                    _uiState.update { UiState.Failure(LoadErrorHandleAction.Retry) }
                }
        }
    }

    fun updateTime(hour: Int, minute: Int) {
        _uiState.updateSuccess { it.copy(hour = hour, minute = minute) }
    }

    fun updateDailyRepeat(isChecked: Boolean) {
        _uiState.updateSuccess {
            it.copy(selectedDays = if (isChecked) ReminderDay.entries.toSet() else emptySet())
        }
    }

    fun toggleDay(day: ReminderDay) {
        _uiState.updateSuccess {
            it.copy(selectedDays = if (day in it.selectedDays) it.selectedDays - day else it.selectedDays + day)
        }
    }

    fun onBackClick() {
        val current = (_uiState.value as? UiState.Success)?.data
        if (current != null && current != savedState) {
            sendSideEffect(NotificationReminderSettingSideEffect.ShowExitDialog)
        } else {
            navigateUpOnce()
        }
    }

    fun onExitConfirm() = navigateUpOnce()

    fun save() {
        val current = (_uiState.value as? UiState.Success)?.data ?: return
        if (current.selectedDays.isEmpty()) {
            sendSideEffect(NotificationReminderSettingSideEffect.ShowToast("반복할 요일을 하나 이상 선택해주세요."))
            return
        }
        if (saveJob?.isActive == true) return

        saveJob = viewModelScope.launch {
            reminderRepository.saveReminder(
                ReminderPreference(
                    isEnabled = true,
                    hour = current.hour,
                    minute = current.minute,
                    selectedDays = current.selectedDays.map { it.dayOfWeek }.toSet(),
                ),
            )
                .onSuccess {
                    savedState = current
                    navigateUpOnce()
                }
                .onLogFailure {
                    sendSideEffect(NotificationReminderSettingSideEffect.ShowToast("저장하지 못했어요. 잠시 후 다시 시도해주세요."))
                }
        }
    }

    private fun navigateUpOnce() {
        if (isNavigatingUp) return
        isNavigatingUp = true
        sendSideEffect(NotificationReminderSettingSideEffect.NavigateUp)
    }

    private fun sendSideEffect(effect: NotificationReminderSettingSideEffect) {
        viewModelScope.launch { _sideEffect.emit(effect) }
    }
}

internal sealed interface NotificationReminderSettingSideEffect {
    data object NavigateUp : NotificationReminderSettingSideEffect
    data object ShowExitDialog : NotificationReminderSettingSideEffect
    data class ShowToast(val text: String) : NotificationReminderSettingSideEffect
}
