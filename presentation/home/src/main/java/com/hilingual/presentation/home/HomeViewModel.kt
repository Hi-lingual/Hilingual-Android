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
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.util.UiState
import com.hilingual.data.calendar.model.CalendarStatus
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.diary.repository.DiaryLocalRepository
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.notification.repository.NotificationRepository
import com.hilingual.data.onboarding.repository.OnboardingRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.home.model.DateUiModel
import com.hilingual.presentation.home.model.toState
import com.hilingual.presentation.home.type.DiaryCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository,
    private val diaryLocalRepository: DiaryLocalRepository,
    private val onboardingRepository: OnboardingRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect.asSharedFlow()

    private val isOnboardingVisible = MutableStateFlow(false)

    private val isRecoveryInProgress = MutableStateFlow(false)

    private val onboardingCheckCompleted = MutableSharedFlow<Unit>(replay = 1)

    private val recoveryReminderResult = MutableStateFlow<Boolean?>(null)

    init {
        checkOnboardingCompleted()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            recoveryReminderResult.update { null }
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
                _uiState.update { UiState.Failure(LoadErrorHandleAction.Retry) }
                recoveryReminderResult.update { false }
                return@launch
            }

            val userInfo = userInfoResult.getOrThrow()
            val calendarData = calendarResult.getOrThrow()

            val initialDates = calendarData.dateList.map { it.toState() }.toImmutableList()
            val initialDiaryContent = fetchDiaryState(today, initialDates, userInfo.recoveryTickets)

            _uiState.update {
                UiState.Success(
                    HomeUiState(
                        header = HomeHeaderUiState(
                            userProfile = userInfo.toState(),
                        ),
                        calendar = HomeCalendarUiState(
                            dates = initialDates,
                            selectedDate = today,
                        ),
                        diaryContent = initialDiaryContent,
                    ),
                )
            }

            checkRecoveryModals(userInfo.recoveryTickets, initialDates)
        }
    }

    fun retryLoad() = loadInitialData()

    fun handleNotificationPermission(
        isGranted: Boolean,
        requiresPermission: Boolean,
    ) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            onboardingCheckCompleted.first()

            if (!isOnboardingVisible.value) {
                showNotificationDialogIfNeeded(isGranted, requiresPermission)
            }
        }
    }

    fun onNotificationPermissionAfterOnboarding(
        isGranted: Boolean,
        requiresPermission: Boolean,
    ) {
        isOnboardingVisible.update { false }
        showNotificationDialogIfNeeded(isGranted, requiresPermission)
    }

    private fun showNotificationDialogIfNeeded(
        isGranted: Boolean,
        requiresPermission: Boolean,
    ) {
        val isPermissionGranted = !requiresPermission || isGranted
        if (isPermissionGranted) return

        viewModelScope.launch {
            if (willShowReminder()) return@launch

            val isAlreadyShown = notificationRepository
                .getIsNotificationDialogShown()
                .getOrDefault(false)
            if (!isAlreadyShown) {
                emitNotificationDialogSideEffect()
            }
        }
    }

    private suspend fun willShowReminder(): Boolean = recoveryReminderResult.first { it != null } == true

    fun onNotificationDialogDismissed() {
        viewModelScope.launch {
            notificationRepository.updateIsNotificationDialogShown(true)
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
                    val newDiaryContent = fetchDiaryState(
                        newDate,
                        newDates,
                        currentState.data.header.userProfile.recoveryTickets,
                    )

                    _uiState.updateSuccess { state ->
                        state.copy(
                            calendar = HomeCalendarUiState(
                                dates = newDates,
                                selectedDate = newDate,
                            ),
                            diaryContent = newDiaryContent,
                        )
                    }
                }
                .onLogFailure {
                    emitErrorDialogSideEffect { onMonthChanged(yearMonth) }
                }
        }
    }

    fun onRecoveryClick(date: LocalDate) {
        if (!isRecoveryInProgress.compareAndSet(expect = false, update = true)) return
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.ShowRewardedAd(date))
        }
    }

    fun onRecoveryAdFinished() {
        isRecoveryInProgress.update { false }
    }

    fun onRewardEarned(date: LocalDate) {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            userRepository.postRecoveryTicket(date)
                .onSuccess { ticket ->
                    _uiState.updateSuccess { state ->
                        val newDates = (state.calendar.dates + DateUiModel(date, CalendarStatus.UNLOCKED))
                            .distinctBy { it.date }
                            .toImmutableList()
                        state.copy(
                            header = state.header.copy(
                                userProfile = state.header.userProfile.copy(
                                    recoveryTickets = ticket.remainingChances,
                                ),
                            ),
                            calendar = state.calendar.copy(dates = newDates),
                        )
                    }
                    updateContentForDate(date)
                    _sideEffect.emit(HomeSideEffect.NavigateToRecoveryWrite(date))
                }
                .onLogFailure {
                    emitToastSideEffect("기록 살리기에 실패했어요. 잠시 후 다시 시도해주세요.")
                }
        }
    }

    fun onRecoveryReminderClosed() {
        recoveryReminderResult.update { false }
    }

    fun onRecoveryReminderConfirmed() {
        val currentState = uiState.value
        if (currentState !is UiState.Success) return

        markReminderShownThisMonth()
        onRecoveryReminderClosed()

        val recentBrokenDate = findRecentBrokenDate(currentState.data.calendar.dates)
        if (recentBrokenDate != null) {
            onDateSelected(recentBrokenDate)
        }
    }

    fun onRecoveryReminderLater() {
        markReminderShownThisMonth()
        onRecoveryReminderClosed()
    }

    private fun markReminderShownThisMonth() {
        viewModelScope.launch {
            onboardingRepository.updateRecoveryReminderLastShownMonth(YearMonth.now().toString())
        }
    }

    private suspend fun checkRecoveryModals(
        recoveryTickets: Int,
        dates: List<DateUiModel>,
    ) {
        onboardingCheckCompleted.first()
        isOnboardingVisible.first { !it }

        val shouldShow = shouldShowReminder(recoveryTickets, dates)
        if (shouldShow) {
            _sideEffect.emit(HomeSideEffect.ShowRecoveryReminder)
        }
        recoveryReminderResult.update { shouldShow }
    }

    private suspend fun shouldShowReminder(
        recoveryTickets: Int,
        dates: List<DateUiModel>,
    ): Boolean {
        if (recoveryTickets <= 0) return false

        val today = LocalDate.now()
        if (!isLastWeekOfMonth(today)) return false

        val currentMonth = YearMonth.now().toString()
        val lastShownMonth = onboardingRepository.getRecoveryReminderLastShownMonth().getOrDefault("")
        if (lastShownMonth == currentMonth) return false

        return hasBrokenDayThisMonth(today, dates)
    }

    private fun isLastWeekOfMonth(today: LocalDate): Boolean =
        today.dayOfMonth > today.lengthOfMonth() - 7

    private fun hasBrokenDayThisMonth(
        today: LocalDate,
        dates: List<DateUiModel>,
    ): Boolean {
        val recordedDates = dates.map { it.date }.toSet()
        val lastBrokenDate = today.minusDays(1)
        var date = today.withDayOfMonth(1)
        while (!date.isAfter(lastBrokenDate)) {
            if (date !in recordedDates) return true
            date = date.plusDays(1)
        }
        return false
    }

    private fun findRecentBrokenDate(dates: List<DateUiModel>): LocalDate? {
        val recordedDates = dates.map { it.date }.toSet()
        val today = LocalDate.now()
        val firstDay = today.withDayOfMonth(1)
        var date = today.minusDays(2)
        while (!date.isBefore(firstDay)) {
            if (date !in recordedDates) return date
            date = date.minusDays(1)
        }
        return null
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
                                diaryThumbnail = state.diaryContent.diaryThumbnail?.copy(isPublished = true),
                            ),
                        )
                    }
                    emitSnackBarSideEffect(
                        message = "일기가 게시되었어요!",
                        actionLabel = "보러가기",
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
                                diaryThumbnail = state.diaryContent.diaryThumbnail?.copy(isPublished = false),
                            ),
                        )
                    }
                    emitToastSideEffect("일기가 비공개 되었어요.")
                }
                .onLogFailure {
                    emitErrorDialogSideEffect { }
                }
        }
    }

    @Deprecated("수정 기능이 도입되기 까지 지원 중단입니다.")
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
                            calendar = state.calendar.copy(dates = newDates),
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
            val newDiaryContent = fetchDiaryState(
                date,
                currentState.data.calendar.dates,
                currentState.data.header.userProfile.recoveryTickets,
            )

            val latestState = uiState.value
            if (latestState !is UiState.Success || latestState.data.calendar.selectedDate != date) return@launch

            _uiState.updateSuccess { state ->
                state.copy(diaryContent = newDiaryContent)
            }

            if (newDiaryContent.cardState == DiaryCardState.WRITTEN && newDiaryContent.diaryThumbnail == null) {
                emitErrorDialogSideEffect { updateContentForDate(date) }
            }
        }
    }

    private suspend fun fetchDiaryState(
        date: LocalDate,
        dates: List<DateUiModel>,
        recoveryTickets: Int,
    ): HomeDiaryUiState = coroutineScope {
        val matchedDate = dates.find { it.date == date }
        val isUnlocked = matchedDate?.status == CalendarStatus.UNLOCKED
        val needsTopic = isUnlocked || DateUiModel(date).isWritable
        val needsThumbnail = matchedDate != null && !isUnlocked

        val tempExistDeferred = async { diaryLocalRepository.isDiaryTempExist(date) }
        val thumbnailDeferred =
            if (needsThumbnail) async { calendarRepository.getDiaryThumbnail(date.toString()) } else null
        val topicDeferred = if (needsTopic) async { calendarRepository.getTopic(date) } else null

        val isTempExist = tempExistDeferred.await().getOrDefault(false)
        val thumbnail = thumbnailDeferred?.await()?.getOrNull()?.toState()
        val topic = topicDeferred?.await()?.getOrNull()?.toState()

        HomeDiaryUiState().update(
            selectedDate = date,
            dates = dates,
            recoveryTickets = recoveryTickets,
            fetchedThumbnail = thumbnail,
            fetchedTopic = topic,
            isTempExist = isTempExist,
        )
    }

    private fun checkOnboardingCompleted() {
        viewModelScope.launch {
            try {
                onboardingRepository.getIsHomeOnboardingCompleted()
                    .onSuccess { isCompleted ->
                        if (!isCompleted) {
                            isOnboardingVisible.update { true }
                            emitOnboardingSideEffect()
                            onboardingRepository.updateIsHomeOnboardingCompleted(true)
                        }
                    }.onLogFailure { }
            } finally {
                onboardingCheckCompleted.emit(Unit)
            }
        }
    }

    private suspend fun emitErrorDialogSideEffect(onRetry: () -> Unit) =
        _sideEffect.emit(HomeSideEffect.ShowErrorDialog(onRetry = onRetry))

    private suspend fun emitToastSideEffect(text: String) =
        _sideEffect.emit(HomeSideEffect.ShowToast(text = text))

    private suspend fun emitSnackBarSideEffect(message: String, actionLabel: String) =
        _sideEffect.emit(HomeSideEffect.ShowSnackBar(message = message, actionLabel = actionLabel))

    private suspend fun emitOnboardingSideEffect() =
        _sideEffect.emit(HomeSideEffect.ShowOnboarding)

    private suspend fun emitNotificationDialogSideEffect() =
        _sideEffect.emit(HomeSideEffect.ShowNotificationDialog)
}

sealed interface HomeSideEffect {
    data class ShowErrorDialog(val onRetry: () -> Unit) : HomeSideEffect

    data object ShowNotificationDialog : HomeSideEffect

    data class ShowToast(val text: String) : HomeSideEffect

    data class ShowSnackBar(val message: String, val actionLabel: String) : HomeSideEffect

    data object ShowOnboarding : HomeSideEffect

    data class ShowRewardedAd(val date: LocalDate) : HomeSideEffect

    data class NavigateToRecoveryWrite(val date: LocalDate) : HomeSideEffect

    data object ShowRecoveryReminder : HomeSideEffect
}
