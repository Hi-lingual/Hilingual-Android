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

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.diary.localstorage.DiaryTempRepository
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.home.model.DateUiModel
import com.hilingual.presentation.home.model.toState
import com.hilingual.presentation.home.type.NotificationPermissionState
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
    private val diaryRepository: DiaryRepository,
    private val diaryTempRepository: DiaryTempRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect.asSharedFlow()

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            val today = LocalDate.now()

            val userInfoDeferred = async {
                userRepository.getUserInfo()
            }
            val calendarDeferred = async {
                calendarRepository.getCalendar(today.year, today.monthValue)
            }

            val userInfoResult = userInfoDeferred.await()
            val calendarResult = calendarDeferred.await()

            delay(200)

            if (userInfoResult.isFailure || calendarResult.isFailure) {
                userInfoResult.onLogFailure { }
                calendarResult.onLogFailure { }
                emitErrorDialogSideEffect { loadInitialData() }
                return@launch
            }

            val userInfo = userInfoResult.getOrThrow()
            val calendarData = calendarResult.getOrThrow()

            val initialDates = calendarData.dateList.map { it.toState() }.toImmutableList()
            val initialDiaryContent = fetchDiaryState(today, initialDates)

            _uiState.update {
                UiState.Success(
                    HomeUiState(
                        header = HomeHeaderUiState(
                            userProfile = userInfo.toState()
                        ),
                        calendar = HomeCalendarUiState(
                            dates = initialDates,
                            selectedDate = today
                        ),
                        diaryContent = initialDiaryContent
                    )
                )
            }
        }
    }

    fun handleNotificationPermission(
        isGranted: Boolean,
        requiresPermission: Boolean
    ) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        val isPermissionGranted = !requiresPermission || isGranted
        val previousState = currentState.data.header.notificationPermissionState

        val newPermissionState = if (isPermissionGranted) {
            NotificationPermissionState.GRANTED
        } else {
            NotificationPermissionState.DENIED
        }

        if (previousState == newPermissionState) return

        _uiState.updateSuccess { state ->
            state.copy(
                header = state.header.copy(notificationPermissionState = newPermissionState)
            )
        }

        if (previousState == NotificationPermissionState.NOT_DETERMINED && !isPermissionGranted) {
            requestNotificationPermission()
        }
    }

    fun onNotificationPermissionResult(isGranted: Boolean) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        val newPermissionState = if (isGranted) {
            NotificationPermissionState.GRANTED
        } else {
            NotificationPermissionState.DENIED
        }

        _uiState.updateSuccess { state ->
            state.copy(
                header = state.header.copy(notificationPermissionState = newPermissionState)
            )
        }
    }

    private fun requestNotificationPermission() {
        viewModelScope.launch {
            _sideEffect.emit(
                HomeSideEffect.RequestNotificationPermission(
                    permission = Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = uiState.value
        if (currentState !is UiState.Success || currentState.data.calendar.selectedDate == date) return

        _uiState.updateSuccess { state ->
            state.copy(calendar = state.calendar.selectDate(date))
        }
        updateContentForDate(date)
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        if (YearMonth.from(currentState.data.calendar.selectedDate) == yearMonth) return

        viewModelScope.launch {
            calendarRepository.getCalendar(yearMonth.year, yearMonth.monthValue)
                .onSuccess { calendarModel ->
                    val today = LocalDate.now()
                    val newDate = if (yearMonth == YearMonth.from(today)) {
                        today
                    } else {
                        yearMonth.atDay(1)
                    }

                    val newDates = calendarModel.dateList.map { data -> data.toState() }.toImmutableList()
                    val newDiaryContent = fetchDiaryState(newDate, newDates)

                    _uiState.updateSuccess { state ->
                        state.copy(
                            calendar = HomeCalendarUiState(
                                dates = newDates,
                                selectedDate = newDate
                            ),
                            diaryContent = newDiaryContent
                        )
                    }
                }
                .onLogFailure {
                    emitErrorDialogSideEffect { onMonthChanged(yearMonth) }
                }
        }
    }

    fun publishDiary(diaryId: Long) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            diaryRepository.patchDiaryPublish(diaryId)
                .onSuccess {
                    _uiState.updateSuccess { state ->
                        state.copy(
                            diaryContent = state.diaryContent.copy(
                                diaryThumbnail = state.diaryContent.diaryThumbnail?.copy(isPublished = true)
                            )
                        )
                    }
                    emitSnackBarSideEffect(
                        message = "일기가 게시되었어요!",
                        actionLabel = "보러가기"
                    )
                }
                .onLogFailure {
                    emitErrorDialogSideEffect { }
                }
        }
    }

    fun unpublishDiary(diaryId: Long) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            diaryRepository.patchDiaryUnpublish(diaryId)
                .onSuccess {
                    _uiState.updateSuccess { state ->
                        state.copy(
                            diaryContent = state.diaryContent.copy(
                                diaryThumbnail = state.diaryContent.diaryThumbnail?.copy(isPublished = false)
                            )
                        )
                    }
                    emitToastSideEffect("일기가 비공개 되었어요.")
                }
                .onLogFailure {
                    emitErrorDialogSideEffect { }
                }
        }
    }

    fun deleteDiary(diaryId: Long) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return
        val selectedDate = currentState.data.calendar.selectedDate

        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId)
                .onSuccess {
                    _uiState.updateSuccess { state ->
                        val newDates = state.calendar.dates
                            .filter { it.date != selectedDate }
                            .toImmutableList()
                        state.copy(
                            calendar = state.calendar.copy(dates = newDates)
                        )
                    }
                    updateContentForDate(selectedDate)
                    emitToastSideEffect("삭제가 완료되었어요.")
                }
                .onLogFailure {
                    emitErrorDialogSideEffect { }
                }
        }
    }

    private fun updateContentForDate(date: LocalDate) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            val newDiaryContent = fetchDiaryState(date, currentState.data.calendar.dates)

            _uiState.updateSuccess { state ->
                state.copy(diaryContent = newDiaryContent)
            }

            val thumbnailResult = calendarRepository.getDiaryThumbnail(date.toString())
            if (thumbnailResult.isFailure && currentState.data.calendar.dates.any { it.date == date }) {
                emitErrorDialogSideEffect { updateContentForDate(date) }
            }
        }
    }

    private suspend fun fetchDiaryState(date: LocalDate, dates: List<DateUiModel>): HomeDiaryUiState {
        val tempExistDeferred = viewModelScope.async { diaryTempRepository.isDiaryTempExist(date) }
        val thumbnailDeferred = viewModelScope.async { calendarRepository.getDiaryThumbnail(date.toString()) }
        val topicDeferred = viewModelScope.async { calendarRepository.getTopic(date.toString()) }

        val isTempExist = tempExistDeferred.await().getOrDefault(false)
        val thumbnail = thumbnailDeferred.await().getOrNull()?.toState()
        val topic = topicDeferred.await().getOrNull()?.toState()

        return HomeDiaryUiState().update(
            selectedDate = date,
            dates = dates,
            fetchedThumbnail = thumbnail,
            fetchedTopic = topic,
            isTempExist = isTempExist
        )
    }

    private suspend fun emitErrorDialogSideEffect(onRetry: () -> Unit) =
        _sideEffect.emit(HomeSideEffect.ShowErrorDialog(onRetry = onRetry))

    private suspend fun emitToastSideEffect(text: String) =
        _sideEffect.emit(HomeSideEffect.ShowToast(text = text))

    private suspend fun emitSnackBarSideEffect(message: String, actionLabel: String) =
        _sideEffect.emit(HomeSideEffect.ShowSnackBar(message = message, actionLabel = actionLabel))
}

sealed interface HomeSideEffect {
    data class ShowErrorDialog(val onRetry: () -> Unit) : HomeSideEffect

    data class ShowToast(val text: String) : HomeSideEffect

    data class ShowSnackBar(val message: String, val actionLabel: String) : HomeSideEffect

    data class RequestNotificationPermission(val permission: String) : HomeSideEffect
}