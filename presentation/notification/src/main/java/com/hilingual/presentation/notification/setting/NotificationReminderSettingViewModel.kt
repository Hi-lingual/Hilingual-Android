package com.hilingual.presentation.notification.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.notification.model.ReminderPreference
import com.hilingual.data.notification.repository.ReminderRepository
import com.hilingual.presentation.notification.setting.model.ReminderDay
import com.hilingual.presentation.notification.setting.model.toReminderDay
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

    init {
        viewModelScope.launch {
            val pref = reminderRepository.getReminder()
            val initial = NotificationReminderSettingUiState(
                hour = pref.hour,
                minute = pref.minute,
                isDailyRepeat = pref.isDailyRepeat,
                selectedDays = pref.selectedDays.map { it.toReminderDay() }.toSet(),
            )
            savedState = initial
            _uiState.update { UiState.Success(initial) }
        }
    }

    fun updateTime(hour: Int, minute: Int) {
        _uiState.updateSuccess { it.copy(hour = hour, minute = minute) }
    }

    fun updateDailyRepeat(isChecked: Boolean) {
        _uiState.updateSuccess {
            it.copy(
                isDailyRepeat = isChecked,
                selectedDays = if (isChecked) ReminderDay.entries.toSet() else emptySet(),
            )
        }
    }

    fun toggleDay(day: ReminderDay) {
        _uiState.updateSuccess {
            val newDays = if (day in it.selectedDays) it.selectedDays - day else it.selectedDays + day
            it.copy(
                selectedDays = newDays,
                isDailyRepeat = newDays.size == ReminderDay.entries.size,
            )
        }
    }

    fun onBackClick() {
        val current = (_uiState.value as? UiState.Success)?.data
        viewModelScope.launch {
            if (current != null && current != savedState) {
                _sideEffect.emit(NotificationReminderSettingSideEffect.ShowExitDialog)
            } else {
                _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp)
            }
        }
    }

    fun onExitConfirm() {
        viewModelScope.launch { _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp) }
    }

    fun save() {
        val current = (_uiState.value as? UiState.Success)?.data ?: return
        if (current.selectedDays.isEmpty()) {
            viewModelScope.launch {
                _sideEffect.emit(NotificationReminderSettingSideEffect.ShowToast("반복할 요일을 하나 이상 선택해주세요."))
            }
            return
        }
        viewModelScope.launch {
            reminderRepository.saveReminder(
                ReminderPreference(
                    isEnabled = true,
                    hour = current.hour,
                    minute = current.minute,
                    isDailyRepeat = current.isDailyRepeat,
                    selectedDays = current.selectedDays.map { it.dayOfWeek }.toSet(),
                ),
            )
            savedState = current
            _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp)
        }
    }
}

internal sealed interface NotificationReminderSettingSideEffect {
    data object NavigateUp : NotificationReminderSettingSideEffect
    data object ShowExitDialog : NotificationReminderSettingSideEffect
    data class ShowToast(val text: String) : NotificationReminderSettingSideEffect
}
