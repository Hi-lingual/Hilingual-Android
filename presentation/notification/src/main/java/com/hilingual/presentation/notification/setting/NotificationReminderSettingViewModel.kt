package com.hilingual.presentation.notification.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.core.localstorage.datasource.ReminderPreferenceDataSource
import com.hilingual.core.localstorage.model.ReminderPreference
import com.hilingual.core.work.scheduler.ReminderScheduler
import com.hilingual.presentation.notification.setting.component.DayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class NotificationReminderSettingViewModel @Inject constructor(
    private val reminderPreferenceDataSource: ReminderPreferenceDataSource,
    private val reminderScheduler: ReminderScheduler,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<NotificationReminderSettingUiState>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationReminderSettingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var savedState: NotificationReminderSettingUiState? = null

    init {
        viewModelScope.launch {
            val pref = reminderPreferenceDataSource.reminderFlow.first()
            val initial = NotificationReminderSettingUiState(
                hour = pref.hour,
                minute = pref.minute,
                isDailyRepeat = pref.isDailyRepeat,
                selectedDays = pref.selectedDays.mapNotNull { name ->
                    runCatching { DayOfWeek.valueOf(name) }.getOrNull()
                }.toSet().ifEmpty { DayOfWeek.entries.toSet() },
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
        viewModelScope.launch {
            val pref = ReminderPreference(
                isEnabled = true,
                hour = current.hour,
                minute = current.minute,
                isDailyRepeat = current.isDailyRepeat,
                selectedDays = current.selectedDays.map { it.name }.toSet(),
            )
            reminderPreferenceDataSource.save(pref)
            reminderScheduler.schedule(pref)
            savedState = current
            _sideEffect.emit(NotificationReminderSettingSideEffect.NavigateUp)
        }
    }
}

internal sealed interface NotificationReminderSettingSideEffect {
    data object NavigateUp : NotificationReminderSettingSideEffect
    data object ShowExitDialog : NotificationReminderSettingSideEffect
}
