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
package com.hilingual.presentation.home.component.calendar.util

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

internal val LocalDate.isFuture: Boolean
    get() = this.isAfter(LocalDate.now())

internal val LocalDate.isWritable: Boolean
    get() {
        val today = LocalDate.now()
        return !this.isAfter(today) && this.isAfter(today.minusDays(2))
    }
