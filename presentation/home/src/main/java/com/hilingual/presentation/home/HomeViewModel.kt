package com.hilingual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.data.calendar.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.async

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

            val userInfoDeferred = async { userRepository.getUserInfo() }

            val userInfoResult = userInfoDeferred.await()

            userInfoResult
                .onSuccess { userInfoModel ->
                    _uiState.value = UiState.Success(
                        HomeUiState(
                            userProfile = userInfoModel.toState(),
                            dateList = persistentListOf()
                        )
                    )
                }
                .onLogFailure { }
        }
    }

    private fun getCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            calendarRepository.getCalendar(year, month)
                .onSuccess { calendarModel ->
                    _uiState.update { currentState ->
                        (currentState as UiState.Success).copy(
                            data = currentState.data.copy(
                                dateList = persistentListOf(*calendarModel.dateList.map { it.toState() }.toTypedArray())
                            )
                        )
                    }
                }
                .onLogFailure { }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { currentState ->
            (currentState as UiState.Success).copy(
                data = currentState.data.copy(selectedDate = date)
            )
        }
        val currentUiState = (_uiState.value as? UiState.Success)?.data
        if (currentUiState?.dateList?.any { LocalDate.parse(it.date) == date } == true) {
            getDiaryThumbnail(date.toString())
        } else {
            _uiState.update { currentState ->
                if (currentState is UiState.Success) {
                    currentState.copy(data = currentState.data.copy(diaryThumbnail = null))
                } else {
                    currentState
                }
            }
        }
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        getCalendarData(yearMonth.year, yearMonth.monthValue)
    }

    private fun getDiaryThumbnail(date: String) {
        viewModelScope.launch {
            calendarRepository.getDiaryThumbnail(date).onSuccess {
                _uiState.update { currentState ->
                    if (currentState is UiState.Success) {
                        currentState.copy(data = currentState.data.copy(
                            diaryThumbnail = it.toState()
                        ))
                    } else {
                        currentState
                    }
                }
            }
            .onLogFailure { }
        }
    }
}