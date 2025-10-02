package com.hilingual.presentation.home.component.calendar.util

import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.ChronoUnit

internal fun DayOfWeek.daysUntil(other: DayOfWeek) = (other.value - value + 7) % 7

internal fun getMonthIndex(startMonth: YearMonth, targetMonth: YearMonth): Int {
    return ChronoUnit.MONTHS.between(startMonth, targetMonth).toInt()
}

internal fun getMonthIndicesCount(startMonth: YearMonth, endMonth: YearMonth): Int {
    return getMonthIndex(startMonth, endMonth) + 1
}
