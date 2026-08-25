package com.hilingual.presentation.widget.streak

import androidx.compose.runtime.Stable
import com.hilingual.data.widget.model.WidgetStreakModel
import java.time.DayOfWeek
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
internal data class StreakUiState(
    val isLoggedIn: Boolean = false,
    val isAvailable: Boolean = false,
    val streakDays: Int = 0,
    val recentDays: ImmutableList<StreakDay> = persistentListOf(),
) {
    companion object {
        fun from(model: WidgetStreakModel): StreakUiState = StreakUiState(
            isLoggedIn = true,
            isAvailable = true,
            streakDays = model.streak,
            recentDays = model.recentDays.map { day ->
                StreakDay(
                    date = day.date,
                    dayOfWeek = day.dayOfWeek,
                    isWritten = day.isWritten,
                )
            }.toImmutableList(),
        )

        val Fake = StreakUiState(
            isLoggedIn = true,
            isAvailable = true,
            streakDays = 4,
            recentDays = fakeRecentDays(writtenDayOffsets = setOf(0L, 1L, 2L, 3L)),
        )

        val Empty = StreakUiState(
            isLoggedIn = true,
            isAvailable = true,
            streakDays = 0,
            recentDays = fakeRecentDays(),
        )

        val Unavailable = StreakUiState(
            isLoggedIn = true,
            recentDays = fakeRecentDays(),
        )

        private fun fakeRecentDays(writtenDayOffsets: Set<Long> = emptySet()): ImmutableList<StreakDay> {
            val today = LocalDate.now()

            return (4L downTo 0L).map { dayOffset ->
                StreakDay(
                    date = today.minusDays(dayOffset),
                    dayOfWeek = today.minusDays(dayOffset).dayOfWeek,
                    isWritten = dayOffset in writtenDayOffsets,
                )
            }.toImmutableList()
        }
    }
}

internal data class StreakDay(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val isWritten: Boolean,
)
