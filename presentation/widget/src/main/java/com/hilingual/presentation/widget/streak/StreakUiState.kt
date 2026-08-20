package com.hilingual.presentation.widget.streak

import androidx.compose.runtime.Stable
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
internal data class StreakUiState(
    val isLoggedIn: Boolean,
    val streakDays: Int,
    val recentDays: ImmutableList<StreakDay>,
) {
    companion object {
        val Fake = StreakUiState(
            isLoggedIn = true,
            streakDays = 4,
            recentDays = fakeRecentDays(writtenDayOffsets = setOf(0L, 1L, 2L, 3L)),
        )

        val LoggedOutFake = StreakUiState(
            isLoggedIn = false,
            streakDays = 0,
            recentDays = fakeRecentDays(),
        )

        val EmptyFake = StreakUiState(
            isLoggedIn = true,
            streakDays = 0,
            recentDays = fakeRecentDays(),
        )

        private fun fakeRecentDays(writtenDayOffsets: Set<Long> = emptySet()): ImmutableList<StreakDay> {
            val today = LocalDate.now()

            return (4L downTo 0L).map { dayOffset ->
                StreakDay(
                    date = today.minusDays(dayOffset),
                    isWritten = dayOffset in writtenDayOffsets,
                )
            }.toImmutableList()
        }
    }
}

internal data class StreakDay(
    val date: LocalDate,
    val isWritten: Boolean,
)
