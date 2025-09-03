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
package com.hilingual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.home.model.toState
import com.hilingual.presentation.home.util.isDateWritable
import com.hilingual.presentation.home.util.isDateWritten
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
import kotlinx.coroutines.flow.update
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
            _uiState.update { UiState.Loading }
            val today = LocalDate.now()

            val userInfoResult = async {
                userRepository.getUserInfo()
            }.await()

            val calendarResult = async {
                calendarRepository.getCalendar(today.year, today.monthValue)
            }.await()

            delay(200)

            val userInfo = userInfoResult.getOrNull()
            val calendarData = calendarResult.getOrNull()

            if (userInfo != null && calendarData != null) {
                _uiState.update {
                    UiState.Success(
                        HomeUiState(
                            userProfile = userInfo.toState(),
                            dateList = calendarData.dateList.map { it.toState() }.toImmutableList(),
                            selectedDate = today,
                            diaryThumbnail = null,
                            todayTopic = null
                        )
                    )
                }
                updateContentForDate(today)
            } else {
                emitRetrySideEffect { loadInitialData() }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = uiState.value
        if (currentState !is UiState.Success || currentState.data.selectedDate == date) return

        _uiState.updateSuccess {
            it.copy(selectedDate = date)
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

        val hasDiary = isDateWritten(date, currentState.data.dateList)
        val isWritable = isDateWritable(date)

        viewModelScope.launch {
            when {
                hasDiary -> {
                    val thumbnail = calendarRepository.getDiaryThumbnail(date.toString())
                        .map { it.toState() }
                        .getOrNull()
                    _uiState.updateSuccess { it.copy(diaryThumbnail = thumbnail) }
                }

                isWritable -> {
                    val topic = calendarRepository.getTopic(date.toString())
                        .map { it.toState() }
                        .getOrNull()
                    _uiState.updateSuccess { it.copy(todayTopic = topic) }
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
