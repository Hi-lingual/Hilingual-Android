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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hilingual.core.common.event.provider.LocalSystemBarsColor
import com.hilingual.core.common.event.trigger.LocalDialogTrigger
import com.hilingual.core.common.extension.collectSideEffect
import com.hilingual.core.common.util.UiState
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.hilingualBlack
import com.hilingual.core.designsystem.theme.white
import com.hilingual.presentation.home.component.HomeHeader
import com.hilingual.presentation.home.component.calendar.HilingualCalendar
import com.hilingual.presentation.home.component.footer.DateTimeInfo
import com.hilingual.presentation.home.component.footer.DiaryDateInfo
import com.hilingual.presentation.home.component.footer.DiaryEmptyCard
import com.hilingual.presentation.home.component.footer.DiaryEmptyCardType
import com.hilingual.presentation.home.component.footer.DiaryPreviewCard
import com.hilingual.presentation.home.component.footer.TodayTopic
import com.hilingual.presentation.home.component.footer.WriteDiaryButton
import com.hilingual.presentation.home.model.DateUiModel
import com.hilingual.presentation.home.model.DiaryThumbnailUiModel
import com.hilingual.presentation.home.model.UserProfileUiModel
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
    val localSystemBarsColor = LocalSystemBarsColor.current
    val dialogEventProvider = LocalDialogTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is HomeSideEffect.ShowRetryDialog -> {
                dialogEventProvider.show(it.onRetry)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    LaunchedEffect(Unit) {
        localSystemBarsColor.setSystemBarColor(
            systemBarsColor = hilingualBlack,
            isDarkIcon = false
        )
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
                onDateSelected = viewModel::onDateSelected,
                onMonthChanged = viewModel::onMonthChanged,
                onWriteDiaryClick = navigateToDiaryWrite,
                onDiaryPreviewClick = navigateToDiaryFeedback
            )
        }

        else -> {}
    }
}

@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    uiState: HomeUiState,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    onWriteDiaryClick: (LocalDate) -> Unit,
    onDiaryPreviewClick: (diaryId: Long) -> Unit
) {
    val date = uiState.selectedDate
    val today = remember { LocalDate.now() }
    val isWritten = remember(uiState.dateList, date) {
        uiState.dateList.any { LocalDate.parse(it.date) == date }
    }

    val isFuture = remember(date, today) { date.isAfter(today) }
    val isWritable = remember(isFuture, date, today) { !isFuture && date.isAfter(today.minusDays(2)) }
    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
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
                    selectedDateProvider = { date },
                    isWrittenProvider = { isWritten }
                )

                if (isWritable) DateTimeInfo(remainingTime = uiState.todayTopic?.remainingTime)
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

@Preview
@Composable
private fun HomeScreenPreview() {
    val uiState = HomeUiState(
        userProfile = UserProfileUiModel(
            profileImg = "",
            nickname = "Hilingual",
            totalDiaries = 10,
            streak = 5
        ),
        selectedDate = LocalDate.now(),
        diaryThumbnail = DiaryThumbnailUiModel(
            diaryId = 1L,
            originalText = "Today I went to the park\n and played with my dog.\n It was a lot of fun.",
            imageUrl = ""
        ),
        dateList = List(10) {
            DateUiModel(date = LocalDate.now().minusDays(it.toLong()).toString())
        }.toPersistentList()
    )
    HilingualTheme {
        HomeScreen(
            paddingValues = PaddingValues(0.dp),
            uiState = uiState,
            onDateSelected = {},
            onMonthChanged = {},
            onWriteDiaryClick = {},
            onDiaryPreviewClick = {}
        )
    }
}
