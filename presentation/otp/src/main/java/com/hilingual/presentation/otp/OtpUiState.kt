package com.hilingual.presentation.otp

import androidx.compose.runtime.Immutable

@Immutable
data class OtpUiState(
    val isLoading: Boolean = false,
    val code: String = "",
    val failureCount: Int = 0,
    val errorMessage: String = ""
)
