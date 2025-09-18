package com.hilingual.presentation.home.component.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hilingual.presentation.home.component.calendar.model.CalendarDay
import com.hilingual.presentation.home.component.calendar.model.CalendarMonth
import com.hilingual.presentation.home.component.calendar.state.CalendarState
import com.hilingual.presentation.home.component.calendar.state.rememberCalendarState
import com.hilingual.presentation.home.component.calendar.util.generateMonthData

@OptIn(ExperimentalFoundationApi::class)
private fun createSnapLayoutInfoProvider(lazyListState: LazyListState) = object : SnapLayoutInfoProvider by SnapLayoutInfoProvider(
    lazyListState = lazyListState,
    snapPosition = SnapPosition.Start
) {
    override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float = 0f
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberSnappingFlingBehavior(lazyListState: LazyListState): FlingBehavior {
    val snappingLayout = remember(lazyListState) { createSnapLayoutInfoProvider(lazyListState) }
    return rememberSnapFlingBehavior(snappingLayout)
}

@Composable
internal fun HorizontalCalendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    dayContent: @Composable (CalendarDay) -> Unit,
    monthHeader: @Composable (CalendarMonth) -> Unit
) {
    val flingBehavior = rememberSnappingFlingBehavior(lazyListState = state.listState)

    LazyRow(
        modifier = modifier,
        state = state.listState,
        flingBehavior = flingBehavior
    ) {
        CalendarMonths(
            monthCount = state.monthIndicesCount,
            monthData = {
                generateMonthData(
                    startMonth = state.startMonth,
                    offset = it,
                    firstDayOfWeek = state.firstDayOfWeek
                ).calendarMonth
            },
            dayContent = dayContent,
            monthHeader = monthHeader
        )
    }
}

private fun LazyListScope.CalendarMonths(
    monthCount: Int,
    monthData: (offset: Int) -> CalendarMonth,
    dayContent: @Composable (CalendarDay) -> Unit,
    monthHeader: @Composable (CalendarMonth) -> Unit
) {
    items(
        count = monthCount,
        key = { offset -> monthData(offset).yearMonth }
    ) { offset ->
        val month = monthData(offset)
        Column(
            modifier = Modifier.fillParentMaxWidth()
        ) {
            monthHeader(month)

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (week in month.weekDays) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (day in week) {
                            Box(
                                modifier = Modifier.size(34.dp)
                            ) {
                                dayContent(day)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
