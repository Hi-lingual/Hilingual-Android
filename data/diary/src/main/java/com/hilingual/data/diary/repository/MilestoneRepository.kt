package com.hilingual.data.diary.repository

import com.hilingual.data.diary.model.Milestone

interface MilestoneRepository {
    fun registerPendingMilestone(totalDiaries: Int)

    fun consumePendingMilestone(): Milestone?
}
