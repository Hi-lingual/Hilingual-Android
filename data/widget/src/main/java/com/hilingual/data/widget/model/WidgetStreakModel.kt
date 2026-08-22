package com.hilingual.data.widget.model

import com.hilingual.data.widget.dto.response.WidgetStreakResponseDto
import java.time.DayOfWeek
import java.time.LocalDate

data class WidgetStreakModel(
    val streak: Int,
    val recentDays: List<WidgetRecentDayModel>,
)

data class WidgetRecentDayModel(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val isWritten: Boolean,
)

internal fun WidgetStreakResponseDto.toModel(): WidgetStreakModel = WidgetStreakModel(
    streak = streak,
    recentDays = recentDays.map { day ->
        WidgetRecentDayModel(
            date = LocalDate.parse(day.date),
            dayOfWeek = DayOfWeek.valueOf(day.dayOfWeek),
            isWritten = day.isWritten,
        )
    },
)
