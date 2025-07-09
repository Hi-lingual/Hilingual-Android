package com.hilingual.presentation.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToDiaryWrite: () -> Unit,
    navigateToDiaryFeedback: (diaryId: Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = hilingualBlack,
            darkIcons = false
        )
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val onDiaryPreviewClickHandler = remember(state.data.diaryPreview) {
                {
                    if (state.data.diaryPreview != null) {
                        navigateToDiaryFeedback(state.data.diaryPreview!!.diaryId)
                    }
                }
            }
            HomeScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onDateSelected = viewModel::onDateSelected,
                onMonthChanged = viewModel::onMonthChanged,
                onWriteDiaryClick = navigateToDiaryWrite,
                onDiaryPreviewClick = onDiaryPreviewClickHandler
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
    onWriteDiaryClick: () -> Unit,
    onDiaryPreviewClick: () -> Unit
) {
    val date = uiState.selectedDate
    val today = remember { LocalDate.now() }
    val isWritten = remember(uiState.writtenDates, date) { uiState.writtenDates.contains(date) }
    val isFuture = remember(date, today) { date.isAfter(today) }
    val isWritable =
        remember(isFuture, date, today) { !isFuture && date.isAfter(today.minusDays(2)) }
    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
            .verticalScroll(verticalScrollState)
    ) {
        HomeHeader(
            imageUrl = uiState.userProfile.profileImg,
            nickname = uiState.userProfile.nickname,
            totalDiaries = uiState.userProfile.totalDiaries,
            streak = uiState.userProfile.streak,
            modifier = Modifier
                .background(hilingualBlack)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 12.dp)
        )

        HilingualCalendar(
            selectedDate = uiState.selectedDate,
            writtenDates = uiState.writtenDates,
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DiaryDateInfo(
                    selectedDateProvider = { date },
                    isWrittenProvider = { isWritten },
                )

                if (isWritable) {
                    val writtenTime = remember(uiState.diaryPreview) {
                        if (uiState.diaryPreview?.createdAt != null) {
                            LocalDateTime.parse(uiState.diaryPreview.createdAt)
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        } else {
                            null
                        }
                    }
                    DateTimeInfo(
                        isWritten = isWritten,
                        writtenTime = writtenTime,
                        remainingTime = uiState.todayTopic?.remainingTime
                    )
                }
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                isWritten -> {
                    if (uiState.diaryPreview != null) {
                        DiaryPreviewCard(
                            diaryText = uiState.diaryPreview.originalText,
                            onClick = onDiaryPreviewClick,
                            imageUrl = uiState.diaryPreview.imageUrl,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                isFuture -> DiaryEmptyCard(type = DiaryEmptyCardType.FUTURE)
                isWritable -> {
                    if (uiState.todayTopic != null) {
                        TodayTopic(
                            koTopic = uiState.todayTopic.topicKo,
                            enTopic = uiState.todayTopic.topicEn,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                        )
                    }
                    WriteDiaryButton(
                        onClick = onWriteDiaryClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> DiaryEmptyCard(type = DiaryEmptyCardType.PAST)
            }
        }
    }
}
