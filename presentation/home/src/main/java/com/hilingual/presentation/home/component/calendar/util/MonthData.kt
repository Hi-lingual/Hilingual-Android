package com.hilingual.presentation.home.component.calendar.util

import com.hilingual.presentation.home.component.calendar.model.CalendarDay
import com.hilingual.presentation.home.component.calendar.model.CalendarMonth
import com.hilingual.presentation.home.component.calendar.model.DayPosition
import java.time.DayOfWeek
import java.time.YearMonth

internal data class MonthData(
    private val month: YearMonth,
    private val inDays: Int,
    private val outDays: Int
) {
    private val totalDays = inDays + month.lengthOfMonth() + outDays
    private val firstDay = month.atStartOfMonth().minusDays(inDays.toLong())
    private val rows = (0 until totalDays).chunked(7)
    private val previousMonth = month.previousMonth
    private val nextMonth = month.nextMonth

    val calendarMonth: CalendarMonth = CalendarMonth(
        month,
        rows.map { week ->
            week.map { dayOffset -> getDay(dayOffset) }
        }
    )

    private fun getDay(dayOffset: Int): CalendarDay {
        val date = firstDay.plusDays(dayOffset.toLong())
        val position = when (date.yearMonth) {
            month -> DayPosition.MonthDate
            previousMonth -> DayPosition.InDate
            nextMonth -> DayPosition.OutDate
            else -> error("Date is in an unexpected month. Should not happen.")
        }
        return CalendarDay(date, position)
    }
}

internal fun generateMonthData(startMonth: YearMonth, offset: Int, firstDayOfWeek: DayOfWeek): MonthData {
    val month = startMonth.plusMonths(offset.toLong())
    val firstDay = month.atStartOfMonth()
    val inDays = firstDayOfWeek.daysUntil(firstDay.dayOfWeek)
    val outDays = (inDays + month.lengthOfMonth()).let { inAndMonthDays ->
        if (inAndMonthDays % 7 != 0) 7 - (inAndMonthDays % 7) else 0
    }
    return MonthData(month, inDays, outDays)
}
