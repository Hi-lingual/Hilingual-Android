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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.analytics.FakeTracker
import com.hilingual.core.common.analytics.Page.HOME
import com.hilingual.core.common.analytics.Tracker
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.model.SnackbarRequest
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.trigger.LocalSnackbarTrigger
import com.hilingual.core.common.trigger.LocalToastTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.home.component.DiaryContinueDialog
import com.hilingual.presentation.home.component.HomeHeader
import com.hilingual.presentation.home.component.calendar.HilingualCalendar
import com.hilingual.presentation.home.component.footer.DiaryDateInfo
import com.hilingual.presentation.home.component.footer.DiaryEmptyCard
import com.hilingual.presentation.home.component.footer.DiaryEmptyCardType
import com.hilingual.presentation.home.component.footer.DiaryPreviewCard
import com.hilingual.presentation.home.component.footer.DiaryTimeInfo
import com.hilingual.presentation.home.component.footer.HomeDropDownMenu
import com.hilingual.presentation.home.component.footer.TodayTopic
import com.hilingual.presentation.home.component.footer.WriteDiaryButton
import com.hilingual.presentation.home.type.DiaryCardState
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToDiaryWrite: (selectedDate: LocalDate) -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    navigateToNotification: () -> Unit,
    navigateToFeedProfile: (userId: Long) -> Unit,
    navigateToFeed: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current
    val toastTrigger = LocalToastTrigger.current
    val snackbarTrigger = LocalSnackbarTrigger.current
    val tracker = LocalTracker.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.ShowErrorDialog -> dialogTrigger.show(sideEffect.onRetry)

            is HomeSideEffect.ShowToast -> toastTrigger(sideEffect.text)

            is HomeSideEffect.ShowSnackBar -> {
                snackbarTrigger(
                    SnackbarRequest(
                        message = sideEffect.message,
                        buttonText = sideEffect.actionLabel,
                        onClick = navigateToFeed
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
        tracker.logEvent(trigger = TriggerType.VIEW, page = HOME, event = "page")
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(white),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            HomeScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onAlarmClick = navigateToNotification,
                onImageClick = {
                    tracker.logEvent(trigger = TriggerType.CLICK, page = HOME, event = "profile")
                    navigateToFeedProfile(0L)
                },
                onDateSelected = viewModel::onDateSelected,
                onMonthChanged = viewModel::onMonthChanged,
                onWriteDiaryClick = { date ->
                    tracker.logEvent(
                        trigger = TriggerType.CLICK,
                        page = HOME,
                        event = "diary_write",
                        properties = mapOf("open_time" to System.currentTimeMillis())
                    )
                    navigateToDiaryWrite(date)
                },
                onClearDiaryTemp = viewModel::onClearDiaryTemp,
                onDiaryPreviewClick = { diaryId ->
                    tracker.logEvent(
                        trigger = TriggerType.VIEW,
                        page = HOME,
                        event = "opend_diary_view",
                        properties = mapOf(
                            "open_time" to System.currentTimeMillis(),
                            "entry_id" to diaryId
                        )
                    )
                    navigateToDiaryFeedback(diaryId)
                },
                onDeleteClick = viewModel::deleteDiary,
                onPublishClick = viewModel::publishDiary,
                onUnpublishClick = viewModel::unpublishDiary,
                tracker = tracker
            )
        }

        else -> {}
    }
}

@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    uiState: HomeUiState,
    onAlarmClick: () -> Unit,
    onImageClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    onWriteDiaryClick: (LocalDate) -> Unit,
    onClearDiaryTemp: (LocalDate) -> Unit,
    onDiaryPreviewClick: (diaryId: Long) -> Unit,
    onDeleteClick: (diaryId: Long) -> Unit,
    onPublishClick: (diaryId: Long) -> Unit,
    onUnpublishClick: (diaryId: Long) -> Unit,
    tracker: Tracker
) {
    val date = uiState.selectedDate
    val verticalScrollState = rememberScrollState()
    var isExpanded by remember { mutableStateOf(false) }
    var isDiaryContinueDialogVisible by remember { mutableStateOf(false) }

    DiaryContinueDialog(
        isVisible = isDiaryContinueDialogVisible,
        onDismiss = { isDiaryContinueDialogVisible = false },
        onNewClick = {
            onClearDiaryTemp(date)
            onWriteDiaryClick(date)
            isDiaryContinueDialogVisible = false
        },
        onContinueClick = {
            onWriteDiaryClick(date)
            isDiaryContinueDialogVisible = false
        }
    )

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .statusBarColor(hilingualBlack)
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(verticalScrollState)
    ) {
        with(uiState) {
            HomeHeader(
                imageUrl = userProfile.profileImg,
                nickname = userProfile.nickname,
                totalDiaries = userProfile.totalDiaries,
                streak = userProfile.streak,
                isNewAlarm = userProfile.isNewAlarm,
                onAlarmClick = onAlarmClick,
                onImageClick = onImageClick,
                modifier = Modifier
                    .background(hilingualBlack)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 12.dp)
            )
        }

        HilingualCalendar(
            selectedDate = uiState.selectedDate,
            writtenDates = uiState.dateList.map { LocalDate.parse(it.date) }.toSet(),
            onDateClick = onDateSelected,
            onMonthChanged = onMonthChanged,
            modifier = Modifier
                .background(HilingualTheme.colors.hilingualBlack)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(white)
                .padding(16.dp)
                .animateContentSize()
        )

        HorizontalDivider(
            thickness = 4.dp,
            color = HilingualTheme.colors.gray100
        )

        Column(
            modifier = Modifier
                .background(HilingualTheme.colors.white)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DiaryDateInfo(
                    selectedDate = date,
                    isPublished = uiState.diaryThumbnail?.isPublished ?: false,
                    isWritten = uiState.cardState == DiaryCardState.WRITTEN,
                    modifier = Modifier.heightIn(min = 20.dp)
                )
                when (uiState.cardState) {
                    DiaryCardState.WRITTEN -> {
                        uiState.diaryThumbnail?.let { diary ->
                            HomeDropDownMenu(
                                isExpanded = isExpanded,
                                isPublished = diary.isPublished,
                                onExpandedChange = {
                                    isExpanded = it
                                    if (it) {
                                        tracker.logEvent(
                                            trigger = TriggerType.CLICK,
                                            page = HOME,
                                            event = "more_menu",
                                            properties = mapOf("menu_name" to "more_menu")
                                        )
                                    }
                                },
                                onDeleteClick = { onDeleteClick(diary.diaryId) },
                                onPublishClick = { onPublishClick(diary.diaryId) },
                                onUnpublishClick = { onUnpublishClick(diary.diaryId) }
                            )
                        }
                    }

                    DiaryCardState.WRITABLE -> DiaryTimeInfo(remainingTime = uiState.todayTopic?.remainingTime)
                    else -> {}
                }
            }

            Spacer(Modifier.height(16.dp))

            with(uiState) {
                when (cardState) {
                    DiaryCardState.WRITTEN -> {
                        if (diaryThumbnail != null) {
                            DiaryPreviewCard(
                                diaryText = diaryThumbnail.originalText,
                                diaryId = diaryThumbnail.diaryId,
                                onClick = onDiaryPreviewClick,
                                imageUrl = diaryThumbnail.imageUrl,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    DiaryCardState.FUTURE -> DiaryEmptyCard(type = DiaryEmptyCardType.FUTURE)
                    DiaryCardState.WRITABLE -> {
                        if (todayTopic != null) {
                            TodayTopic(
                                koTopic = todayTopic.topicKo,
                                enTopic = todayTopic.topicEn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize()
                                    .noRippleClickable {
                                        tracker.logEvent(
                                            trigger = TriggerType.CLICK,
                                            page = HOME,
                                            event = "switch_language",
                                            properties = mapOf(
                                                "recommen_topic" to "${todayTopic.topicKo}/${todayTopic.topicEn}"
                                            )
                                        )
                                    }
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        WriteDiaryButton(
                            onClick = {
                                if (uiState.hasDiaryTemp) {
                                    isDiaryContinueDialogVisible = true
                                } else {
                                    onWriteDiaryClick(date)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    DiaryCardState.REWRITE_DISABLED,
                    DiaryCardState.PAST -> DiaryEmptyCard(type = DiaryEmptyCardType.PAST)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HilingualTheme {
        HomeScreen(
            paddingValues = PaddingValues(),
            uiState = HomeUiState.Fake,
            onAlarmClick = {},
            onImageClick = {},
            onDateSelected = {},
            onMonthChanged = {},
            onWriteDiaryClick = {},
            onClearDiaryTemp = {},
            onDiaryPreviewClick = {},
            onDeleteClick = {},
            onPublishClick = {},
            onUnpublishClick = {},
            tracker = FakeTracker()
        )
    }
}
