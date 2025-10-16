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
import java.time.YearMonth
import java.time.temporal.ChronoUnit

internal fun DayOfWeek.daysUntil(other: DayOfWeek) = (other.value - value + 7) % 7

internal fun getMonthIndex(startMonth: YearMonth, targetMonth: YearMonth): Int {
    return ChronoUnit.MONTHS.between(startMonth, targetMonth).toInt()
}

internal fun getMonthIndicesCount(startMonth: YearMonth, endMonth: YearMonth): Int {
    return getMonthIndex(startMonth, endMonth) + 1
}
