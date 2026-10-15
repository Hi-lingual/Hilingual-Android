package com.hilingual.data.diary.repository

import com.hilingual.core.common.model.Milestone

interface MilestoneRepository {
    fun registerPendingMilestone(totalDiaries: Int)

    fun consumePendingMilestone(): Milestone?
}
