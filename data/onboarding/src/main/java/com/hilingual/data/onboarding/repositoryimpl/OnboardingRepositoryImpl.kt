package com.hilingual.data.onboarding.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.localstorage.OnboardingStateManager
import com.hilingual.data.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingStateManager: OnboardingStateManager
) : OnboardingRepository {
    override suspend fun getIsHomeOnboardingCompleted(): Result<Boolean> =
        suspendRunCatching {
            onboardingStateManager.getIsHomeOnboardingCompleted()
        }

    override suspend fun updateIsHomeOnboardingCompleted(isCompleted: Boolean): Result<Unit> =
        suspendRunCatching {
            onboardingStateManager.updateIsHomeOnboardingCompleted(isCompleted = isCompleted)
        }
}