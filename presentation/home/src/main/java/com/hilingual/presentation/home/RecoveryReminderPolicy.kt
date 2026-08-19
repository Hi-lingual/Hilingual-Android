/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.presentation.home

import com.hilingual.presentation.home.model.DateUiModel
import java.time.LocalDate
import java.time.YearMonth

internal object RecoveryReminderPolicy {

    fun shouldShowReminder(
        recoveryTickets: Int,
        dates: List<DateUiModel>,
        today: LocalDate,
        lastShownMonth: String,
    ): Boolean {
        if (recoveryTickets <= 0) return false

        val isLastWeek = today.dayOfMonth > today.lengthOfMonth() - 7
        if (!isLastWeek) return false

        if (lastShownMonth == YearMonth.from(today).toString()) return false

        return hasBrokenDayThisMonth(today, dates)
    }

    fun findRecentBrokenDate(
        dates: List<DateUiModel>,
        today: LocalDate,
    ): LocalDate? {
        val recordedDates = dates.map { it.date }.toSet()
        val firstDay = today.withDayOfMonth(1)
        var date = today.minusDays(2)
        while (!date.isBefore(firstDay)) {
            if (date !in recordedDates) return date
            date = date.minusDays(1)
        }
        return null
    }

    private fun hasBrokenDayThisMonth(
        today: LocalDate,
        dates: List<DateUiModel>,
    ): Boolean {
        val recordedDates = dates.map { it.date }.toSet()
        val lastBrokenDate = today.minusDays(1)
        var date = today.withDayOfMonth(1)
        while (!date.isAfter(lastBrokenDate)) {
            if (date !in recordedDates) return true
            date = date.plusDays(1)
        }
        return false
    }
}
