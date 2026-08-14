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
package com.hilingual.presentation.home.component.calendar.state

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hilingual.presentation.home.component.calendar.util.firstDayOfWeekFromLocale
import com.hilingual.presentation.home.component.calendar.util.getMonthIndex
import com.hilingual.presentation.home.component.calendar.util.getMonthIndicesCount
import java.time.DayOfWeek
import java.time.YearMonth

private val CALENDAR_START_MONTH = YearMonth.of(2025, 1)
private val CALENDAR_END_MONTH = YearMonth.of(2100, 12)

@Composable
internal fun rememberCalendarState(
    initialVisibleMonth: YearMonth = YearMonth.now(),
    firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale(),
): CalendarState {
    val pagerState = rememberPagerState(
        initialPage = getMonthIndex(CALENDAR_START_MONTH, initialVisibleMonth),
    ) {
        getMonthIndicesCount(CALENDAR_START_MONTH, CALENDAR_END_MONTH)
    }
    return remember { CalendarState(pagerState, firstDayOfWeek) }
}

@Stable
internal class CalendarState(
    val pagerState: PagerState,
    internal val firstDayOfWeek: DayOfWeek,
) {
    internal val startMonth: YearMonth = CALENDAR_START_MONTH

    val targetMonth: YearMonth
        get() = monthForPage(pagerState.targetPage)

    fun monthForPage(page: Int): YearMonth = startMonth.plusMonths(page.toLong())

    private fun pageForMonth(month: YearMonth): Int =
        getMonthIndex(startMonth, month).coerceIn(0, pagerState.pageCount - 1)

    suspend fun scrollToMonth(month: YearMonth) {
        pagerState.scrollToPage(pageForMonth(month))
    }

    suspend fun animateScrollToMonth(month: YearMonth) {
        pagerState.animateScrollToPage(pageForMonth(month))
    }
}
