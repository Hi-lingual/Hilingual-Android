package com.hilingual.presentation.home.component.calendar.model

import androidx.compose.runtime.Immutable
import java.time.YearMonth

@Immutable
internal data class CalendarMonth(
    val yearMonth: YearMonth,
    val weekDays: List<List<CalendarDay>>
)
