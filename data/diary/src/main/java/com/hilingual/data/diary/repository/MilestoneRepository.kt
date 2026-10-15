package com.hilingual.data.diary.repository

import com.hilingual.core.common.model.Milestone

interface MilestoneRepository {
    fun registerPendingMilestone(milestone: Milestone)

    fun consumePendingMilestone(): Milestone?
}
