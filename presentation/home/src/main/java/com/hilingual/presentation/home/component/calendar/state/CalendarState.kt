package com.hilingual.presentation.home.component.calendar.state

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hilingual.presentation.home.component.calendar.model.CalendarMonth
import com.hilingual.presentation.home.component.calendar.util.firstDayOfWeekFromLocale
import com.hilingual.presentation.home.component.calendar.util.generateMonthData
import com.hilingual.presentation.home.component.calendar.util.getMonthIndex
import com.hilingual.presentation.home.component.calendar.util.getMonthIndicesCount
import java.time.DayOfWeek
import java.time.YearMonth

private val CALENDAR_START_MONTH = YearMonth.of(2025, 1)
private val CALENDAR_END_MONTH = YearMonth.of(2100, 12)

@Composable
internal fun rememberCalendarState(
    initialVisibleMonth: YearMonth = YearMonth.now(),
    firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale()
): CalendarState {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = getMonthIndex(CALENDAR_START_MONTH, initialVisibleMonth)
    )
    return remember { CalendarState(listState, firstDayOfWeek) }
}

@Stable
internal class CalendarState(
    val listState: LazyListState,
    internal val firstDayOfWeek: DayOfWeek
) {
    internal val startMonth: YearMonth = CALENDAR_START_MONTH

    val firstVisibleMonth: CalendarMonth by derivedStateOf {
        generateMonthData(
            startMonth = startMonth,
            offset = listState.firstVisibleItemIndex,
            firstDayOfWeek = firstDayOfWeek
        ).calendarMonth
    }

    val monthIndicesCount: Int = getMonthIndicesCount(startMonth, CALENDAR_END_MONTH)

    suspend fun scrollToMonth(month: YearMonth) {
        listState.scrollToItem(getMonthIndex(startMonth, month))
    }

    suspend fun animateScrollToMonth(month: YearMonth) {
        listState.animateScrollToItem(getMonthIndex(startMonth, month))
    }
}
