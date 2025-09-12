package com.hilingual.presentation.home.component.calendar.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
internal data class CalendarDay(
    val date: LocalDate,
    val position: DayPosition
)
