package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.bottomsheet.HilingualYearMonthPickerBottomSheet
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.collections.immutable.toImmutableList
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
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek().toImmutableList() }
    val coroutineScope = rememberCoroutineScope()
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfRow
    )

    if (isBottomSheetVisible) {
        HilingualYearMonthPickerBottomSheet(
            initialYearMonth = state.firstVisibleMonth.yearMonth,
            onDismiss = { isBottomSheetVisible = false },
            onDateSelected = { newYearMonth ->
                coroutineScope.launch {
                    state.scrollToMonth(newYearMonth)
                }
                isBottomSheetVisible = false
            }
        )
    }

    LaunchedEffect(state.firstVisibleMonth) {
        onMonthChanged(state.firstVisibleMonth.yearMonth)
    }

    Column(
        modifier = modifier
    ) {
        CalendarHeader(
            onDownArrowClick = { isBottomSheetVisible = true },
            onLeftArrowClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.minusMonths(1))
                }
            },
            onRightArrowClick = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.plusMonths(1))
                }
            },
            yearMonth = { state.firstVisibleMonth.yearMonth },
            modifier = Modifier.padding(bottom = 12.dp)
        )

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
