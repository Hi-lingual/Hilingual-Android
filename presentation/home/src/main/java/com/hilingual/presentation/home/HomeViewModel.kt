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
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.home.model.toState
import com.hilingual.presentation.home.util.isDateWritable
import com.hilingual.presentation.home.util.isDateWritten
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
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
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect.asSharedFlow()

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            val today = LocalDate.now()

            val userInfoDeferred = async { userRepository.getUserInfo() }
            val calendarDeferred = async { calendarRepository.getCalendar(today.year, today.monthValue) }

            val userInfoResult = userInfoDeferred.await()
            val calendarResult = calendarDeferred.await()

            delay(200)

            if (userInfoResult.isFailure || calendarResult.isFailure) {
                userInfoResult.onLogFailure {}
                calendarResult.onLogFailure {}
                emitRetrySideEffect { loadInitialData() }
                return@launch
            }

            val userInfo = userInfoResult.getOrThrow()
            val calendarData = calendarResult.getOrThrow()

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

    fun publishDiary() {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return
        val diaryId = currentState.data.diaryThumbnail?.diaryId ?: return

        viewModelScope.launch {
            diaryRepository.patchDiaryPublish(diaryId)
                .onSuccess {
                    _uiState.updateSuccess {
                        it.copy(
                            diaryThumbnail = it.diaryThumbnail?.copy(isPublished = true)
                        )
                    }
                    emitSnackBarSideEffect(
                        message = "일기가 게시되었어요!",
                        actionLabel = "보러가기"
                    )
                }
                .onLogFailure {
                    emitRetrySideEffect { }
                }
        }
    }

    fun unpublishDiary() {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return
        val diaryId = currentState.data.diaryThumbnail?.diaryId ?: return

        viewModelScope.launch {
            diaryRepository.patchDiaryUnpublish(diaryId)
                .onSuccess {
                    _uiState.updateSuccess {
                        it.copy(
                            diaryThumbnail = it.diaryThumbnail?.copy(isPublished = false)
                        )
                    }
                    emitToastSideEffect("일기가 비공개 되었어요.")
                }
                .onLogFailure {
                    emitRetrySideEffect { }
                }
        }
    }

    fun deleteDiary() {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return
        val diaryId = currentState.data.diaryThumbnail?.diaryId ?: return
        val selectedDate = currentState.data.selectedDate

        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId)
                .onSuccess {
                    _uiState.updateSuccess { state ->
                        val newDateList =
                            state.dateList.filter { it.date != selectedDate.toString() }
                                .toImmutableList()
                        state.copy(
                            dateList = newDateList
                        )
                    }
                    updateContentForDate(selectedDate)
                    emitToastSideEffect("삭제가 완료되었어요.")
                }
                .onLogFailure {
                    emitRetrySideEffect { }
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
                    calendarRepository.getDiaryThumbnail(date.toString())
                        .onSuccess { thumbnail ->
                            _uiState.updateSuccess { it.copy(diaryThumbnail = thumbnail.toState()) }
                        }
                        .onLogFailure { }
                }

                isWritable -> {
                    calendarRepository.getTopic(date.toString())
                        .onSuccess { topic ->
                            _uiState.updateSuccess { it.copy(todayTopic = topic.toState()) }
                        }
                        .onLogFailure { }
                }
            }
        }
    }

    private suspend fun emitRetrySideEffect(onRetry: () -> Unit) =
        _sideEffect.emit(HomeSideEffect.ShowRetryDialog(onRetry = onRetry))

    private suspend fun emitToastSideEffect(text: String) =
        _sideEffect.emit(HomeSideEffect.ShowToast(text = text))

    private suspend fun emitSnackBarSideEffect(message: String, actionLabel: String) =
        _sideEffect.emit(HomeSideEffect.ShowSnackBar(message = message, actionLabel = actionLabel))
}

sealed interface HomeSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : HomeSideEffect

    data class ShowToast(val text: String) : HomeSideEffect

    data class ShowSnackBar(val message: String, val actionLabel: String) : HomeSideEffect
}
