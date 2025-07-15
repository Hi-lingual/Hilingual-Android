package com.hilingual.presentation.onboarding

import androidx.compose.runtime.Immutable

@Immutable
data class OnboardingUiState(
    val nickname: String = "",
    val validationMessage: String = "",
    val isNicknameValid: Boolean = false
)
