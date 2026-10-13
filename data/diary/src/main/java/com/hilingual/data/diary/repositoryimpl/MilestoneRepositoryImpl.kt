package com.hilingual.data.diary.repositoryimpl

import com.hilingual.data.diary.model.Milestone
import com.hilingual.data.diary.repository.MilestoneRepository
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class MilestoneRepositoryImpl @Inject constructor() : MilestoneRepository {
    private val pendingMilestone = AtomicReference<Milestone?>(null)

    override fun registerPendingMilestone(totalDiaries: Int) {
        Milestone.from(totalDiaries)?.let(pendingMilestone::set)
    }

    override fun consumePendingMilestone(): Milestone? = pendingMilestone.getAndSet(null)
}
