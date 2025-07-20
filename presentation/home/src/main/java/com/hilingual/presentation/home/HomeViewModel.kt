package com.hilingual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.home.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect.asSharedFlow()

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val today = LocalDate.now()

            val userInfoDeferred = async { userRepository.getUserInfo() }
            val calendarDeferred = async { calendarRepository.getCalendar(today.year, today.monthValue) }

            val userInfoResult = userInfoDeferred.await()
            val calendarResult = calendarDeferred.await()
            delay(200)
            userInfoResult.onSuccess { userInfo ->
                calendarResult.onSuccess { calendarData ->
                    _uiState.value = UiState.Success(
                        HomeUiState(
                            userProfile = userInfo.toState(),
                            dateList = calendarData.dateList.map { it.toState() }.toImmutableList(),
                            selectedDate = today,
                            diaryThumbnail = null,
                            todayTopic = null
                        )
                    )
                    updateContentForDate(today)
                }.onLogFailure {
                    emitRetrySideEffect { loadInitialData() }
                }
            }.onLogFailure {
                emitRetrySideEffect { loadInitialData() }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = uiState.value
        if (currentState !is UiState.Success || currentState.data.selectedDate == date) return

        _uiState.updateSuccess {
            it.copy(
                selectedDate = date,
                diaryThumbnail = null,
                todayTopic = null
            )
        }
        updateContentForDate(date)
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        val currentState = uiState.value
        if (currentState !is UiState.Success || YearMonth.from(currentState.data.selectedDate) == yearMonth) return

        viewModelScope.launch {
            calendarRepository.getCalendar(yearMonth.year, yearMonth.monthValue)
                .onSuccess { calendarModel ->
                    val newDate = yearMonth.atDay(1)
                    _uiState.updateSuccess {
                        it.copy(
                            dateList = calendarModel.dateList.map { data -> data.toState() }
                                .toImmutableList(),
                            selectedDate = newDate,
                            diaryThumbnail = null,
                            todayTopic = null
                        )
                    }
                    updateContentForDate(newDate)
                }
                .onLogFailure {
                    emitRetrySideEffect { onMonthChanged(yearMonth) }
                }
        }
    }

    private fun updateContentForDate(date: LocalDate) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        val hasDiary = currentState.data.dateList.any { LocalDate.parse(it.date) == date }
        val isWritable = !date.isAfter(LocalDate.now()) && date.isAfter(LocalDate.now().minusDays(2))

        viewModelScope.launch {
            when {
                hasDiary -> {
                    calendarRepository.getDiaryThumbnail(date.toString())
                        .onSuccess { thumbnail ->
                            _uiState.updateSuccess { it.copy(diaryThumbnail = thumbnail.toState()) }
                        }
                        .onLogFailure {
                            _uiState.updateSuccess { it.copy(diaryThumbnail = null) }
                        }
                }
                isWritable -> {
                    calendarRepository.getTopic(date.toString())
                        .onSuccess { topic ->
                            _uiState.updateSuccess { it.copy(todayTopic = topic.toState()) }
                        }
                        .onLogFailure {
                            _uiState.updateSuccess { it.copy(todayTopic = null) }
                        }
                }
                else -> {
                    _uiState.updateSuccess {
                        it.copy(diaryThumbnail = null, todayTopic = null)
                    }
                }
            }
        }
    }

    private fun emitRetrySideEffect(onRetry: () -> Unit) {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.ShowRetryDialog(onRetry = onRetry))
        }
    }
}

sealed interface HomeSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : HomeSideEffect
}
