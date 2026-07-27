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
package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.bottomsheet.HilingualYearMonthPickerBottomSheet
import com.hilingual.presentation.home.component.calendar.state.rememberCalendarState
import com.hilingual.presentation.home.component.calendar.util.atStartOfMonth
import com.hilingual.presentation.home.component.calendar.util.daysOfWeek
import com.hilingual.presentation.home.component.calendar.util.daysUntil
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun HilingualCalendar(
    selectedDate: LocalDate?,
    writtenDates: ImmutableSet<LocalDate>,
    onDateClick: (date: LocalDate) -> Unit,
    onMonthChanged: (yearMonth: YearMonth) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val daysOfWeek = remember { daysOfWeek().toImmutableList() }
    val today = remember { LocalDate.now() }
    val initialMonth = remember { YearMonth.from(today) }
    val state = rememberCalendarState(
        initialVisibleMonth = initialMonth,
        firstDayOfWeek = daysOfWeek.first(),
    )
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    var displayedMonth by remember { mutableStateOf(initialMonth) }
    val isDragged by state.pagerState.interactionSource.collectIsDraggedAsState()
    val currentOnMonthChanged by rememberUpdatedState(onMonthChanged)

    LaunchedEffect(state) {
        snapshotFlow { isDragged to state.targetMonth }
            .collect { (dragged, month) ->
                if (!dragged && month != displayedMonth) {
                    displayedMonth = month
                    currentOnMonthChanged(month)
                }
            }
    }

    HilingualYearMonthPickerBottomSheet(
        isVisible = isBottomSheetVisible,
        initialYearMonth = displayedMonth,
        onDismiss = { isBottomSheetVisible = false },
        onDateSelected = { newYearMonth ->
            coroutineScope.launch {
                state.scrollToMonth(newYearMonth)
                isBottomSheetVisible = false
            }
        },
    )

    val weekCount = remember(displayedMonth) {
        val inDays = daysOfWeek.first().daysUntil(displayedMonth.atStartOfMonth().dayOfWeek)
        (inDays + displayedMonth.lengthOfMonth() + 6) / 7
    }

    Column(modifier = modifier) {
        CalendarHeader(
            onDownArrowClick = { isBottomSheetVisible = true },
            onLeftArrowClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.targetMonth.minusMonths(1))
                }
            },
            onRightArrowClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.targetMonth.plusMonths(1))
                }
            },
            yearMonth = { displayedMonth },
            modifier = Modifier.padding(bottom = 12.dp),
        )

        DaysOfWeekTitle(daysOfWeek = daysOfWeek)

        Spacer(Modifier.height(8.dp))

        HorizontalCalendar(
            state = state,
            modifier = Modifier
                .height(calendarGridHeight(weekCount))
                .background(HilingualTheme.colors.white),
            dayContent = { day ->
                DayItem(
                    day = day,
                    onClick = { onDateClick(day.date) },
                    isSelected = selectedDate == day.date,
                    isWritten = day.date in writtenDates,
                    isToday = day.date == today,
                )
            },
        )
    }
}

@Preview
@Composable
private fun HilingualCalendarPreview() {
    HilingualTheme {
        var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
        val writtenDates = remember {
            persistentSetOf(LocalDate.now().minusDays(2), LocalDate.now().plusDays(3))
        }
        HilingualCalendar(
            selectedDate = selectedDate,
            writtenDates = writtenDates,
            onDateClick = { selectedDate = it },
            onMonthChanged = { },
        )
    }
}
