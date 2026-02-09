package com.hilingual.data.onboarding.repository

interface OnboardingRepository {
    suspend fun getIsHomeOnboardingCompleted(): Result<Boolean>
    suspend fun updateIsHomeOnboardingCompleted(isCompleted: Boolean): Result<Unit>

    suspend fun getIsSplashOnboardingCompleted(): Result<Boolean>
    suspend fun completeSplashOnboarding(): Result<Unit>
}
