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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hilingual.presentation.home.component.calendar.model.CalendarDay
import com.hilingual.presentation.home.component.calendar.model.CalendarMonth
import com.hilingual.presentation.home.component.calendar.state.CalendarState
import com.hilingual.presentation.home.component.calendar.util.generateMonthData

@Composable
internal fun HorizontalCalendar(
    state: CalendarState,
    dayContent: @Composable (CalendarDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = state.pagerState,
        key = { it },
        beyondViewportPageCount = 1,
        verticalAlignment = Alignment.Top,
        modifier = modifier,
    ) { page ->
        val month = remember(page) {
            generateMonthData(
                startMonth = state.startMonth,
                offset = page,
                firstDayOfWeek = state.firstDayOfWeek,
            ).calendarMonth
        }

        MonthContent(
            month = month,
            dayContent = dayContent,
        )
    }
}

internal fun calendarGridHeight(weekCount: Int): Dp = (DAY_CELL_SIZE + WEEK_BOTTOM_SPACING) * weekCount

@Composable
private fun MonthContent(
    month: CalendarMonth,
    dayContent: @Composable (CalendarDay) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        for (week in month.weekDays) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                for (day in week) {
                    Box(
                        modifier = Modifier.size(DAY_CELL_SIZE),
                    ) {
                        dayContent(day)
                    }
                }
            }
            Spacer(Modifier.height(WEEK_BOTTOM_SPACING))
        }
    }
}

private val DAY_CELL_SIZE = 34.dp
private val WEEK_BOTTOM_SPACING = 12.dp
