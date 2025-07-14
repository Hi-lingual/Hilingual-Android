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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val today = LocalDate.now()

            val userInfoDeferred = async { userRepository.getUserInfo() }
            val calendarDeferred = async { calendarRepository.getCalendar(today.year, today.monthValue) }

            val userInfoResult = userInfoDeferred.await()
            val calendarResult = calendarDeferred.await()

            userInfoResult.onSuccess { userInfo ->
                calendarResult.onSuccess { calendarData ->
                    val hasDiaryToday = calendarData.dateList.any { LocalDate.parse(it.date) == today }
                    _uiState.value = UiState.Success(
                        HomeUiState(
                            userProfile = userInfo.toState(),
                            dateList = calendarData.dateList.map { it.toState() }.toImmutableList(),
                            selectedDate = today,
                            isDiaryThumbnailLoading = hasDiaryToday,
                            diaryThumbnail = null
                        )
                    )
                    if (hasDiaryToday) {
                        getDiaryThumbnail(today.toString())
                    }
                    return@launch
                }.onLogFailure { }
            }.onLogFailure { }
        }
    }

    private fun getCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            calendarRepository.getCalendar(year, month)
                .onSuccess { calendarModel ->
                    _uiState.updateSuccess {
                        it.copy(dateList = calendarModel.dateList.map { it.toState() }.toImmutableList())
                    }
                }
                .onLogFailure { }
        }
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = _uiState.value
        if (currentState !is UiState.Success) return

        if (currentState.data.selectedDate == date) return

        val hasDiary = currentState.data.dateList.any { LocalDate.parse(it.date) == date }

        _uiState.updateSuccess {
            it.copy(
                selectedDate = date,
                diaryThumbnail = null,
                isDiaryThumbnailLoading = hasDiary
            )
        }

        if (hasDiary) {
            getDiaryThumbnail(date.toString())
        }
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        getCalendarData(yearMonth.year, yearMonth.monthValue)
    }

    private fun getDiaryThumbnail(date: String) {
        viewModelScope.launch {
            calendarRepository.getDiaryThumbnail(date)
                .onSuccess { thumbnail ->
                    _uiState.updateSuccess {
                        it.copy(
                            diaryThumbnail = thumbnail.toState(),
                            isDiaryThumbnailLoading = false
                        )
                    }
                }
                .onLogFailure {
                    _uiState.updateSuccess {
                        it.copy(isDiaryThumbnailLoading = false)
                    }
                }
        }
    }
}