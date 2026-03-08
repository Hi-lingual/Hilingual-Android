package com.hilingual.data.onboarding.localstorage

interface OnboardingLocalDataSource {
    suspend fun getIsHomeOnboardingCompleted(): Boolean
    suspend fun updateIsHomeOnboardingCompleted(isCompleted: Boolean)
    suspend fun getIsSplashOnboardingCompleted(): Boolean
    suspend fun updateIsSplashOnboardingCompleted(isCompleted: Boolean)
}
