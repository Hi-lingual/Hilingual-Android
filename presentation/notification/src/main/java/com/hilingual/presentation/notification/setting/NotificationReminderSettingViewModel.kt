package com.hilingual.presentation.notification.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.notification.setting.component.DayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class NotificationReminderSettingViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<NotificationReminderSettingUiState>>(
        UiState.Success(NotificationReminderSettingUiState()),
    )
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationReminderSettingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    // TODO: 로직 PR에서 ReminderPreferenceDataSource로부터 초기값 로드

    fun updateTime(hour: Int, minute: Int) {
        _uiState.updateSuccess { it.copy(hour = hour, minute = minute) }
    }

    fun updateDailyRepeat(isChecked: Boolean) {
        _uiState.updateSuccess {
            it.copy(
                isDailyRepeat = isChecked,
                selectedDays = if (isChecked) DayOfWeek.entries.toSet() else emptySet(),
            )
        }
    }

    fun toggleDay(day: DayOfWeek) {
        val current = (_uiState.value as? UiState.Success)?.data ?: return
        if (current.isDailyRepeat) return

        _uiState.updateSuccess {
            val newDays = if (day in it.selectedDays) it.selectedDays - day else it.selectedDays + day
            it.copy(selectedDays = newDays)
        }
    }

    fun onBackClick() {
        // TODO: dirty 체크 후 ShowExitDialog / NavigateUp 분기
        viewModelScope.launch { _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp) }
    }

    fun onExitConfirm() {
        viewModelScope.launch { _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp) }
    }

    fun save() {
        // TODO: ReminderPreferenceDataSource.save + ReminderScheduler.schedule 연동
        viewModelScope.launch { _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp) }
    }
}

internal sealed interface NotificationReminderSettingSideEffect {
    data object NavigateUp : NotificationReminderSettingSideEffect
    data object ShowExitDialog : NotificationReminderSettingSideEffect
}
