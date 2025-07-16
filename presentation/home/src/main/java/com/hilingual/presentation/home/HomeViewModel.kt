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
                    } else {
                        getTopic(today.toString())
                    }
                    return@launch
                }.onLogFailure { }
            }.onLogFailure {
                _sideEffect.emit(HomeSideEffect.ShowRetryDialog {})
            }
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
                isDiaryThumbnailLoading = hasDiary,
                todayTopic = null
            )
        }

        // TODO: QA 이후 시연용으로 변경 필요 by. 민재
        val today = LocalDate.now()
        val isWritable = !date.isAfter(today) && date.isAfter(today.minusDays(2))

        when {
            hasDiary -> getDiaryThumbnail(date.toString())
            isWritable -> getTopic(date.toString())
        }

        // TODO: QA 이후 시연용으로 변경 필요 by. 민재
        // if (hasDiary) {
        //    getDiaryThumbnail(date.toString())
        //} else {
        //    getTopic(date.toString())
        //}
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        val currentState = _uiState.value
        if (currentState !is UiState.Success) return

        if (YearMonth.from(currentState.data.selectedDate) == yearMonth) return

        viewModelScope.launch {
            _uiState.updateSuccess { it.copy(isDiaryThumbnailLoading = true) }
            calendarRepository.getCalendar(yearMonth.year, yearMonth.monthValue)
                .onSuccess { calendarModel ->
                    val newDate = yearMonth.atDay(1)
                    val hasDiaryOnFirst = calendarModel.dateList.any { LocalDate.parse(it.date) == newDate }

                    _uiState.updateSuccess { it ->
                        it.copy(
                            dateList = calendarModel.dateList.map { it.toState() }.toImmutableList(),
                            selectedDate = newDate,
                            diaryThumbnail = null,
                            todayTopic = null,
                            isDiaryThumbnailLoading = hasDiaryOnFirst
                        )
                    }

                    // TODO: QA 이후 시연용으로 변경 필요 by. 민재
                    val today = LocalDate.now()
                    val isWritable = !newDate.isAfter(today) && newDate.isAfter(today.minusDays(2))
                    when {
                        hasDiaryOnFirst -> getDiaryThumbnail(newDate.toString())
                        isWritable -> getTopic(newDate.toString())
                    }

                    // TODO: QA 이후 시연용으로 변경 필요 by. 민재
                    // if (hasDiaryOnFirst) {
                    //     getDiaryThumbnail(newDate.toString())
                    // } else {
                    //    getTopic(newDate.toString())
                    //}
                }
                .onLogFailure {
                    _sideEffect.emit(HomeSideEffect.ShowRetryDialog {})
                }
        }
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
                    _sideEffect.emit(HomeSideEffect.ShowRetryDialog {})
                }
        }
    }

    private fun getTopic(date: String) {
        viewModelScope.launch {
            calendarRepository.getTopic(date)
                .onSuccess { topic ->
                    _uiState.updateSuccess {
                        it.copy(todayTopic = topic.toState())
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(HomeSideEffect.ShowRetryDialog {})
                }
        }
    }
}

sealed interface HomeSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : HomeSideEffect
}
