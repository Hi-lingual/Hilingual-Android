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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    viewModel: HomeViewModel = hiltViewModel(),
    onWriteDiaryClick: () -> Unit,
    onDiaryPreviewClick: (diaryId: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            HomeScreen(
                paddingValues = paddingValues,
                uiState = state.data,
                onDateSelected = viewModel::onDateSelected,
                onMonthChanged = viewModel::onMonthChanged,
                onWriteDiaryClick = onWriteDiaryClick,
                onDiaryPreviewClick = {
                    state.data.diaryPreview?.let { onDiaryPreviewClick(it.diaryId) }
                }
            )
        }

        else -> { /* Handle Empty and Failure states if necessary */
        }
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
    val today = LocalDate.now()
    val isWritten = uiState.writtenDates.contains(date)
    val isFuture = date.isAfter(today)
    val isWritable = !isFuture && date.isAfter(today.minusDays(3))
    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(HilingualTheme.colors.white)
            .padding(paddingValues)
            .verticalScroll(verticalScrollState)
    ) {
        HomeHeader(
            imageUrl = uiState.userProfile.profileImg,
            nickName = uiState.userProfile.nickname,
            totalDiaries = uiState.userProfile.totalDiaries,
            streak = uiState.userProfile.streak,
            modifier = Modifier
                .background(hilingualBlack)
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
                    val writtenTime = uiState.diaryPreview?.createdAt?.let {
                        LocalDateTime.parse(it).format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                    DateTimeInfo(
                        isWritten = isWritten,
                        writtenTime = writtenTime,
                        remainingTime = uiState.todayTopic?.remainingTime
                    )
                }
            }

            if (isWritten) {
                uiState.diaryPreview?.let { preview ->
                    DiaryPreviewCard(
                        diaryText = preview.originalText,
                        onClick = onDiaryPreviewClick,
                        imageUrl = preview.imageUrl,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                when {
                    isFuture -> DiaryEmptyCard(type = DiaryEmptyCardType.FUTURE)
                    isWritable -> {
                        uiState.todayTopic?.let { topic ->
                            TodayTopic(
                                koTopic = topic.topicKor,
                                enTopic = topic.topicEn,
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
}
