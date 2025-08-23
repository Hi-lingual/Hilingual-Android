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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.extension.statusBarColor
import com.hilingual.core.common.trigger.LocalDialogTrigger
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.white
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
import com.hilingual.presentation.home.model.DateUiModel
import com.hilingual.presentation.home.model.DiaryThumbnailUiModel
import com.hilingual.presentation.home.model.TodayTopicUiModel
import com.hilingual.presentation.home.model.UserProfileUiModel
import com.hilingual.presentation.home.util.isDateFuture
import com.hilingual.presentation.home.util.isDateWritable
import com.hilingual.presentation.home.util.isDateWritten
import kotlinx.collections.immutable.toPersistentList
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToDiaryWrite: (selectedDate: LocalDate) -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogTrigger = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.ShowRetryDialog -> {
                dialogTrigger.show(sideEffect.onRetry)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
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
                onAlarmClick = { /* TODO: 알람 스크린으로 이동 by.angrypodo*/ },
                onDateSelected = viewModel::onDateSelected,
                onMonthChanged = viewModel::onMonthChanged,
                onWriteDiaryClick = navigateToDiaryWrite,
                onDiaryPreviewClick = navigateToDiaryFeedback,
                onDeleteClick = { /* TODO: 삭제 by.angrypodo*/ },
                onPublishClick = { /* TODO: 게시 by.angrypodo*/ },
                onUnpublishClick = { /* TODO: 비게시 by.angrypodo*/ }
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
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    onWriteDiaryClick: (LocalDate) -> Unit,
    onDiaryPreviewClick: (diaryId: Long) -> Unit,
    onDeleteClick: () -> Unit,
    onPublishClick: () -> Unit,
    onUnpublishClick: () -> Unit
) {
    val date = uiState.selectedDate
    val verticalScrollState = rememberScrollState()

    val isWritten = remember(uiState.dateList, date) {
        isDateWritten(date, uiState.dateList)
    }
    val isFuture = remember(date) { isDateFuture(date) }
    val isWritable = remember(date) { isDateWritable(date) }
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .statusBarColor(hilingualBlack)
            .background(HilingualTheme.colors.white)
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
                    isWritten = isWritten
                )
                when {
                    isWritten -> HomeDropDownMenu(
                        isExpanded = isExpanded,
                        isPublished = uiState.diaryThumbnail?.isPublished ?: false,
                        onExpandedChange = { isExpanded = it },
                        onDeleteClick = onDeleteClick,
                        onPublishClick = onPublishClick,
                        onUnpublishClick = onUnpublishClick
                    )
                    isWritable -> DiaryTimeInfo(remainingTime = uiState.todayTopic?.remainingTime)
                }
            }

            Spacer(Modifier.height(16.dp))

            with(uiState) {
                when {
                    isWritten -> {
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

                    isFuture -> DiaryEmptyCard(type = DiaryEmptyCardType.FUTURE)

                    isWritable -> {
                        if (todayTopic != null) {
                            TodayTopic(
                                koTopic = todayTopic.topicKo,
                                enTopic = todayTopic.topicEn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize()
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        WriteDiaryButton(
                            onClick = { onWriteDiaryClick(date) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> DiaryEmptyCard(type = DiaryEmptyCardType.PAST)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Interactive HomeScreen Preview")
@Composable
private fun InteractiveHomeScreenPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var writtenDiaries by remember {
        mutableStateOf(
            mapOf(
                LocalDate.now() to DiaryThumbnailUiModel(
                    diaryId = 1L,
                    originalText = "Today's diary.",
                    imageUrl = "",
                    isPublished = false
                ),
                LocalDate.now().minusDays(2) to DiaryThumbnailUiModel(
                    diaryId = 2L,
                    originalText = "Diary from 2 days ago.",
                    imageUrl = "",
                    isPublished = true
                ),
                LocalDate.now().minusDays(3) to DiaryThumbnailUiModel(
                    diaryId = 3L,
                    originalText = "Diary from 3 days ago.",
                    imageUrl = "",
                    isPublished = false
                )
            )
        )
    }

    val writtenDates = writtenDiaries.keys
    val diaryForSelectedDate = writtenDiaries[selectedDate]
    val isWritten = selectedDate in writtenDates
    val isWritable = remember(selectedDate) { isDateWritable(selectedDate) }

    val uiState = HomeUiState(
        userProfile = UserProfileUiModel(
            profileImg = "",
            nickname = "Podo",
            totalDiaries = writtenDates.size,
            streak = 5
        ),
        selectedDate = selectedDate,
        dateList = writtenDates.map { DateUiModel(it.toString()) }.toPersistentList(),
        diaryThumbnail = diaryForSelectedDate,
        todayTopic = if (!isWritten && isWritable) {
            TodayTopicUiModel(
                topicKo = "인터랙티브 주제",
                topicEn = "Interactive Topic",
                remainingTime = 1000
            )
        } else {
            null
        }
    )

    HilingualTheme {
        HomeScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = uiState,
            onDateSelected = { newDate ->
                selectedDate = newDate
            },
            onMonthChanged = {},
            onWriteDiaryClick = {},
            onDiaryPreviewClick = {},
            onAlarmClick = {},
            onDeleteClick = {
                writtenDiaries = writtenDiaries - selectedDate
            },
            onPublishClick = {
                writtenDiaries[selectedDate]?.let {
                    writtenDiaries = writtenDiaries + (selectedDate to it.copy(isPublished = true))
                }
            },
            onUnpublishClick = {
                writtenDiaries[selectedDate]?.let {
                    writtenDiaries = writtenDiaries + (selectedDate to it.copy(isPublished = false))
                }
            }
        )
    }
}
