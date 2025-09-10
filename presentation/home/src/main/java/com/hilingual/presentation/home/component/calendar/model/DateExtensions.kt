package com.hilingual.presentation.home.component.calendar.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

internal fun daysOfWeek(firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale()): List<DayOfWeek> {
    val pivot = 7 - firstDayOfWeek.ordinal
    val daysOfWeek = DayOfWeek.entries
    return daysOfWeek.takeLast(pivot) + daysOfWeek.dropLast(pivot)
}

internal fun firstDayOfWeekFromLocale(locale: Locale = Locale.getDefault()): DayOfWeek =
    WeekFields.of(locale).firstDayOfWeek

internal fun YearMonth.atStartOfMonth(): LocalDate = this.atDay(1)

internal val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)

internal val YearMonth.nextMonth: YearMonth
    get() = this.plusMonths(1)

internal val YearMonth.previousMonth: YearMonth
    get() = this.minusMonths(1)
