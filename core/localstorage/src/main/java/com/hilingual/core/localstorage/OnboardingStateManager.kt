package com.hilingual.core.localstorage

interface OnboardingStateManager {
    suspend fun getIsHomeOnboardingCompleted(): Boolean
    suspend fun updateIsHomeOnboardingCompleted(isCompleted: Boolean)
}
