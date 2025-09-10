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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.bottomsheet.HilingualYearMonthPickerBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.home.component.calendar.model.daysOfWeek
import com.hilingual.presentation.home.component.calendar.state.rememberCalendarState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun HilingualCalendar(
    selectedDate: LocalDate?,
    writtenDates: Set<LocalDate>,
    onDateClick: (date: LocalDate) -> Unit,
    onMonthChanged: (yearMonth: YearMonth) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val daysOfWeek = remember { daysOfWeek().toImmutableList() }
    val initialMonth = remember { YearMonth.now() }
    val state = rememberCalendarState(
        initialVisibleMonth = initialMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var settledMonth by remember { mutableStateOf(initialMonth) }

    LaunchedEffect(state.listState) {
        snapshotFlow { state.listState.isScrollInProgress }
            .filter { !it }
            .collect {
                val newMonth = state.firstVisibleMonth.yearMonth
                if (newMonth != settledMonth) {
                    settledMonth = newMonth
                    onMonthChanged(newMonth)
                }
            }
    }

    HilingualYearMonthPickerBottomSheet(
        isVisible = isBottomSheetVisible,
        initialYearMonth = settledMonth,
        onDismiss = { isBottomSheetVisible = false },
        onDateSelected = { newYearMonth ->
            if (newYearMonth != settledMonth) {
                settledMonth = newYearMonth
                onMonthChanged(newYearMonth)
            }
            coroutineScope.launch {
                state.scrollToMonth(newYearMonth)
                isBottomSheetVisible = false
            }
        }
    )

    Column(modifier = modifier) {
        CalendarHeader(
            onDownArrowClick = { isBottomSheetVisible = true },
            onLeftArrowClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(settledMonth.minusMonths(1))
                }
            },
            onRightArrowClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(settledMonth.plusMonths(1))
                }
            },
            yearMonth = { settledMonth },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        key(settledMonth) {
            HorizontalCalendar(
                state = state,
                modifier = Modifier.background(HilingualTheme.colors.white),
                monthHeader = {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                },
                dayContent = { day ->
                    DayItem(
                        day = day,
                        onClick = { onDateClick(day.date) },
                        isSelected = selectedDate == day.date,
                        isWritten = day.date in writtenDates
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun HilingualCalendarPreview() {
    HilingualTheme {
        var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
        val writtenDates = remember {
            setOf(LocalDate.now().minusDays(2), LocalDate.now().plusDays(3))
        }
        HilingualCalendar(
            selectedDate = selectedDate,
            writtenDates = writtenDates,
            onDateClick = { selectedDate = it },
            onMonthChanged = { }
        )
    }
}
