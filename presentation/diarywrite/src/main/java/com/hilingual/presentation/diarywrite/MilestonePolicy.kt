package com.hilingual.presentation.diarywrite

internal object MilestonePolicy {
    private val EARLY_MILESTONES = setOf(7, 30, 50)
    private const val REGULAR_START = 100
    private const val REGULAR_INTERVAL = 50

    fun isMilestone(totalDiaries: Int): Boolean =
        totalDiaries in EARLY_MILESTONES ||
            (totalDiaries >= REGULAR_START && totalDiaries % REGULAR_INTERVAL == 0)
}
