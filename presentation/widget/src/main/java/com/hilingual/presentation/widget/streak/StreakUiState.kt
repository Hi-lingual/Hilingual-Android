package com.hilingual.presentation.widget.streak

internal data class StreakUiState(
    val isLoggedIn: Boolean,
    val streakDays: Int,
    val recentDays: List<StreakDay>,
)

internal data class StreakDay(
    val label: String,
    val isWritten: Boolean,
)
